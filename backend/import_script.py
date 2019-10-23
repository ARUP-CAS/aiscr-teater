import codecs
import errno
import json
import markdown
import os
import psycopg2
import sys
import unidecode
import urllib.parse
import uuid
from elasticsearch_dsl import Index, Document, Text, analyzer, Completion
from elasticsearch_dsl.connections import connections

print(
        "Connect to POSTGRES host=" + os.environ['DB_ADDRESS'] + " port=" + os.environ['DB_PORT'] + " dbname=" + os.environ[
    'DB_NAME'])
connection = psycopg2.connect(
    "host=" + os.environ['DB_ADDRESS'] + " port=" + os.environ['DB_PORT'] + " dbname=" + os.environ[
        'DB_NAME'] + " user=" + os.environ['DB_USER'] + " password=" + os.environ['DB_PASSWORD'])

print("Connect to ELASTICSEARCH host=" + os.environ['ELASTIC_ADDRESS'] + " port=" + os.environ['ELASTIC_PORT'])
client = connections.create_connection(hosts=[os.environ['ELASTIC_ADDRESS'] + ':' + os.environ['ELASTIC_PORT']])
cursor = connection.cursor()

## just in case the default recursion limit is too low
# sys.setrecursionlimit(16000)

# delete the index to prevent duplicates, ignore if it doesn't exist
index = Index('category')
index.delete(ignore=404)

my_analyzer = analyzer('my_analyzer',
                       tokenizer="standard",
                       filter=["lowercase", "asciifolding"]
                       )


class Category(Document):
    id = Text()
    name = Text(analyzer=my_analyzer)
    name_suggest = Completion(analyzer=my_analyzer)
    category_type = Text()
    description = Text(analyzer=my_analyzer)
    url = Text()

    class Index:
        name = 'category'
        settings = {
            "number_of_shards": 1,
        }

    def save(self, **kwargs):
        return super(Category, self).save(**kwargs)


Category.init()


def get_description_from_list(desc_list):
    if desc_list is None:
        return ''
    return '\n *'.join([part.replace("\n", "<br>") for part in ''.join(desc_list).split("\n *")]).replace('\n *', '\n \n*')


def insert_rec(category, parent_id, parent_url):
    generated_id = str(uuid.uuid4())
    to_insert = {'parent_id': parent_id,
                 'id': generated_id,
                 'is_leaf': 'subcategories' not in category.keys(),
                 'description': get_description_from_list(category.get('description')),
                 'name': category.get('name', ''),
                 'category_type': category_type_from_color(category.get('color')),
                 'date_from': category.get('date_range', {}).get('from'),
                 'date_to': category.get('date_range', {}).get('to'),
                 'date_accurate': category.get('date_accurate')}

    # set url
    to_insert['url'] = make_url(parent_url, to_insert['name'])

    [insert_rec(item, generated_id, to_insert['url']) for item in category.get('subcategories', list())]

    if not os.path.exists("./html"):
        try:
            os.makedirs("html")
        except OSError as exc:  # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise

    # markdown to html file
    text_file = codecs.open("./html/" + to_insert['id'] + ".html", "w", encoding="utf-8")
    if to_insert['description'] is not None:
        text_file.write(markdown.markdown(to_insert['description']))
    text_file.close()
    if to_insert['description'] is not None:
        to_insert['description'] = markdown.markdown(to_insert['description'])

    # elasticsearch indexing
    category_obj = Category()
    category_obj.id = to_insert['id']
    category_obj.categoryType = to_insert['category_type']
    category_obj.description = to_insert['description']
    category_obj.name = to_insert['name']
    category_obj.url = to_insert['url']
    if to_insert['name'] is not None:
        category_obj.name_suggest = to_insert['name'].split()

    category_obj.save()

    # postgresql insert
    insert_query = "INSERT INTO category( id, category_type, date_from, date_to, date_accurate, description, name, url, " \
                   "parent_id, is_leaf)" \
                   " VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"

    cursor.execute(insert_query, (to_insert['id'],
                                  to_insert['category_type'],
                                  to_insert['date_from'],
                                  to_insert['date_to'],
                                  to_insert['date_accurate'],
                                  to_insert['description'],
                                  to_insert['name'],
                                  to_insert['url'],
                                  to_insert['parent_id'],
                                  to_insert['is_leaf']))
    connection.commit()


def make_url(parent_url, name):
    v = name.lstrip().replace(" (", "-").replace("( ", "-").replace(", ", "-").replace(" ", "-").replace(
        "(", "-").replace(")", "-").replace(";", "").replace("--", "-")
    if v[-1] == "-":
        v = v[:-1]
    ascii_data = unidecode.unidecode(v)
    url = urllib.parse.quote(ascii_data.lower())
    constrain_map = {}
    if parent_url is not None:
        together = parent_url + "/" + url

        together = together.replace("-/", "/").replace("---", "-").replace("--", "-")

        if together[-1] == "-":
            together = together[:-1]

        if together in constrain_map:
            constrain_map[together] += 1
            together = together + "-" + str(constrain_map[together])
        else:
            constrain_map[together] = 1
        return together
    return url


def category_type_from_color(color):
    return {
        '#008000': 'NATIONAL',
        '#ffa500': 'LOCAL',
        '#6af6d7': 'BLANK'
    }.get(color, 'OTHER')


def check_table_exists(dbcon, tablename):
    dbcur = dbcon.cursor()
    dbcur.execute("""
        SELECT COUNT(*)
        FROM information_schema.tables
        WHERE table_name = '{0}'
        """.format(tablename.replace('\'', '\'\'')))
    if dbcur.fetchone()[0] == 1:
        dbcur.close()
        return True

    dbcur.close()
    return False


def drop_and_create_table_if_exists(recreate):
    if recreate:
        if check_table_exists(connection, "category"):
            drop_query = "DROP TABLE category"
            cursor.execute(drop_query)

            connection.commit()

        create_query = "CREATE TABLE category (id VARCHAR(255) PRIMARY KEY, category_type VARCHAR(15), date_from BIGINT, " \
                       "date_to BIGINT, date_accurate BIGINT, description VARCHAR(16383), name VARCHAR(255), " \
                       "url VARCHAR(255), parent_id VARCHAR(255), is_leaf BOOLEAN) "

        cursor.execute(create_query)

        connection.commit()


drop_and_create_table_if_exists(True)

print("Process data...")

for filename in sorted(os.listdir(os.getcwd() + '/json'), key=lambda a: int(a.split("_")[1].split(".")[0])):
    with open('./json/' + filename, 'r', encoding='utf-8') as f:
        for data in json.load(f):
            insert_rec(data, None, None)

print("Import finished")
