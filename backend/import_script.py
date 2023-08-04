import itertools
import json
import os
import urllib.parse
import uuid

import psycopg2
import unidecode
import unicodedata
from elasticsearch_dsl import Index, Document, Text, analyzer, Completion, Keyword
from elasticsearch_dsl.connections import connections

print(
    "Connect to POSTGRES host=" + os.environ['DB_ADDRESS'] + " port=" + os.environ['DB_PORT'] + " dbname=" + os.environ[
        'DB_NAME'])
connection = psycopg2.connect(
    "host=" + os.environ['DB_ADDRESS'] +
    " port=" + os.environ['DB_PORT'] +
    " dbname=" + os.environ['DB_NAME'] +
    " user=" + os.environ['DB_USER'] +
    " password=" + os.environ['DB_PASSWORD'])

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
    id = Keyword()
    name_cs = Text(analyzer=my_analyzer)
    name_en = Text(analyzer=my_analyzer)
    name_de = Text(analyzer=my_analyzer)
    name_cs_suggest = Completion(analyzer=my_analyzer)
    name_en_suggest = Completion(analyzer=my_analyzer)
    name_de_suggest = Completion(analyzer=my_analyzer)
    url = Keyword()
    metadata = Text(analyzer=my_analyzer)

    class Index:
        name = 'category'
        settings = {
            "number_of_shards": 1,
        }

    def save(self, **kwargs):
        return super(Category, self).save(**kwargs)


Category.init()


def process_category(category, parent_id, parent_url):
    category_id = category.get('id')
    category_insert = {'parent_id': parent_id,
                       'id': category_id,
                       'is_leaf': 'subcategories' not in category.keys(),
                       'name_cs': category.get('name').get('cz'),
                       'name_en': category.get('name').get('en'),
                       'name_de': category.get('name').get('de'),
                       'date_from': category.get('date_range', {}).get('from') or None,
                       'date_to': category.get('date_range', {}).get('to') or None,
                       'date_accurate': category.get('date_accurate') or None}

    category_insert['url'] = make_url(parent_url, category_insert['name_cs'])

    # postgresql insert
    insert_query = "INSERT INTO category( id, date_from, date_to, date_accurate, name_cs, name_en, name_de, url, " \
                   "parent_id, leaf)" \
                   " VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"

    cursor.execute(insert_query, (category_insert['id'],
                                  category_insert['date_from'],
                                  category_insert['date_to'],
                                  category_insert['date_accurate'],
                                  category_insert['name_cs'],
                                  category_insert['name_en'],
                                  category_insert['name_de'],
                                  category_insert['url'],
                                  category_insert['parent_id'],
                                  category_insert['is_leaf']))
    connection.commit()

    [process_category(item, category_id, category_insert['url']) for item in category.get('subcategories') or list()]

    # prepare metadata for indexing
    description_result = list(itertools.chain.from_iterable(
        [process_description(item, category_id) for item in category.get('description') or list()]))
    metadata = unicodedata.normalize('NFC', ';'.join(
        filter(None, description_result
               + list([str(category_insert['date_from']),
                       str(category_insert['date_to']),
                       str(category_insert['date_accurate'])]))))

    # elasticsearch indexing
    category_obj = Category()
    category_obj.id = category_insert['id']
    category_obj.name_cs = category_insert['name_cs']
    category_obj.name_en = category_insert['name_en']
    category_obj.name_de = category_insert['name_de']
    if category_insert['name_cs'] is not None:
        category_obj.name_cs_suggest = [category_insert['name_cs']] + category_insert['name_cs'].split()
    if category_insert['name_en'] is not None:
        category_obj.name_en_suggest = [category_insert['name_en']] + category_insert['name_en'].split()
    if category_insert['name_de'] is not None:
        category_obj.name_de_suggest = [category_insert['name_de']] + category_insert['name_de'].split()
    category_obj.metadata = metadata
    category_obj.url = category_insert['url']
    category_obj.save()


def process_description(description, category_id):
    description_id = str(uuid.uuid4())
    description_insert = {
        'id': description_id,
        'title_property_key': description.get('title').get('text'),
        'category_id': category_id
    }

    insert_query = "INSERT INTO description( id, category_id, title_property_key)" \
                   " VALUES (%s, %s, %s);"

    cursor.execute(insert_query, (description_insert['id'],
                                  description_insert['category_id'],
                                  description_insert['title_property_key']))
    connection.commit()
    content_result = [process_content(item, description_id) for item in description.get('content') or list()]
    # return content for indexing
    return list(itertools.chain.from_iterable(content_result))


def process_content(content, description_id):
    text_element = content.get('text')
    localized_content_result = list()
    if isinstance(text_element, str):
        localized_content_result = process_content_localized(content, description_id, None, text_element)
    if isinstance(text_element, dict):
        localized_content_result = list(itertools.chain.from_iterable([
            process_content_localized(content, description_id, language, text_element.get(language)) for language
            in text_element.keys()]))
    # return content for indexing
    return localized_content_result


def process_content_localized(content, description_id, language, text):
    content_id = str(uuid.uuid4())
    if not text:
        return ''
    content_insert = {
        'id': content_id,
        'text': text,
        'description_id': description_id,
        'language': None if language is None else 'CS' if language == 'cz' else language.upper()
    }
    insert_query = "INSERT INTO content( id, description_id, text, language)" \
                   " VALUES (%s, %s, %s, %s);"
    cursor.execute(insert_query, (content_insert['id'],
                                  content_insert['description_id'],
                                  content_insert['text'],
                                  content_insert['language']))
    connection.commit()

    quotes = content.get('quotes')
    if isinstance(quotes, list):
        quotes_result = [process_quote(quote, content_id) for quote in quotes]
    if isinstance(quotes, dict):
        quotes_result = [process_quote(quote, content_id) for quote in quotes.get(language) or list()]
    # return content for indexing
    return quotes_result + [content_insert['text']]


def process_quote(quote, content_id):
    quote_id = str(uuid.uuid4())
    quote_insert = {
        'id': quote_id,
        'content_id': content_id,
        'source_id': quote.get('id') or None,
        'title': quote.get('title_page') or None,
        'date': quote.get('date') or None,
        'location_page': (quote.get('location') or {}).get('page') or None,
        'location_url': (quote.get('location') or {}).get('url') or None
    }
    if not quote_insert['title'] and not quote_insert['date'] and not quote_insert['location_page'] and not quote_insert['location_url']:
        return ''

    insert_query = "INSERT INTO quote( id, content_id, source_id, title, date, location_page, location_url)" \
                   " VALUES (%s, %s, %s, %s, %s, %s, %s);"
    cursor.execute(insert_query, (quote_insert['id'],
                                  quote_insert['content_id'],
                                  quote_insert['source_id'],
                                  quote_insert['title'],
                                  quote_insert['date'],
                                  quote_insert['location_page'],
                                  quote_insert['location_url']))
    connection.commit()

    # return content for indexing
    return quote_insert['title']


def process_source(source):
    source_insert = {
        'id': source.get('id'),
        'label': source.get('label'),
        'url': source.get('url'),
    }

    insert_query = "INSERT INTO source( id, label, url)" \
                   " VALUES (%s, %s, %s);"
    cursor.execute(insert_query, (source_insert['id'],
                                  source_insert['label'],
                                  source_insert['url']))
    connection.commit()


def make_url(parent_url, name):
    v = name.lstrip().replace(" (", "-").replace("( ", "-").replace(", ", "-").replace(" ", "-").replace(
        "(", "-").replace(")", "-").replace(";", "").replace("--", "-").replace("*", "")
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


def recreate_db_schema():
    cursor.execute("select exists(select schema_name FROM information_schema.schemata where schema_name='thesaurus')")
    if cursor.fetchone()[0]:
        cursor.execute("DROP SCHEMA thesaurus CASCADE")
        connection.commit()

    create_query = """
        create schema thesaurus;
        set search_path to thesaurus;
        create table category
        (
          id            varchar(255) not null
            constraint category_pk
              primary key,
          date_from     varchar(255),
          date_to       varchar(255),
          date_accurate varchar(255),
          url           varchar(511),
          parent_id     varchar(255),
          leaf          boolean,
          name_cs       varchar(255),
          name_en       varchar(255),
          name_de       varchar(255)
        );

        create table description
        (
          id                 varchar(255) not null
            constraint description_pk
              primary key,
          category_id        varchar(255)
            constraint description_category_id_fk
              references category (id),
          title_property_key varchar(255)
        );

        create table content
        (
          id             varchar(255) not null
            constraint content_pk
              primary key,
          description_id varchar(255) not null
            constraint content_description_id_fk
              references description,
          text              text,
          language          varchar(255)
        );

        create table source
        (
          id    varchar(255) not null
            constraint source_pk
              primary key,
          label text,
          url   text
        );

        create table quote
        (
          id            varchar(255) not null
            constraint quote_pk
              primary key,
          content_id    varchar(255) not null
            constraint quote_content_id_fk
              references content,
          source_id     varchar(255)
            constraint quote_source_id_fk
              references source,
          title         text,
          date          varchar(255),
          location_page varchar(255),
          location_url  text
        );

        create table history_event
        (
          id           varchar(255) not null
            constraint history_event_pk
              primary key,
          label        varchar(255),
          timestamp    timestamp
        );
    """

    cursor.execute(create_query)

    connection.commit()


def is_import_json(filename):
    return "import" in filename


def list_duplicates():
    cursor.execute("SELECT category.name_cs FROM category GROUP BY name_cs HAVING COUNT(*) > 1")
    duplicates_cs = cursor.fetchall()
    cursor.execute("SELECT category.name_en FROM category GROUP BY name_en HAVING COUNT(*) > 1")
    duplicates_en = cursor.fetchall()
    cursor.execute("SELECT category.name_de FROM category GROUP BY name_de HAVING COUNT(*) > 1")
    duplicates_de = cursor.fetchall()
    return "cs: {cs} \nen: {en} \nde: {de}".format(cs=duplicates_cs, en=duplicates_en, de=duplicates_de).encode('utf-8')


recreate_db_schema()

print("Processing data...")

with open('./json/bibliografie.json', 'r', encoding='utf-8') as f:
    for source in json.load(f):
        process_source(source)

for filename in sorted(filter(is_import_json, os.listdir(os.getcwd() + '/json')),
                       key=lambda a: int(a.split("_")[1].split(".")[0])):
    with open('./json/' + filename, 'r', encoding='utf-8') as f:
        for data in json.load(f):
            process_category(data, None, None)

cursor.execute(
    "INSERT INTO history_event(id, label, timestamp) VALUES ('" + str(uuid.uuid4()) + "', 'last_import_date', now());")
connection.commit()

print("Import finished")

with open("duplicates.txt", 'w') as out:
    out.write(str(list_duplicates()))
