# ARUB application

# Setup:
1. **Run PostgreSQL DB and ElasticSearch**
    - use ElasticSearch version 6.8.2

2. **Set environment variables:**
    - DB_ADDRESS
    - DB_PORT
    - DB_USER
    - DB_PASSWORD
    - DB_NAME
    - ELASTIC_ADDRESS
    - ELASTIC_PORT

3. **Import content data:**
    - put data source jsons in `/backend/json` dir
    - run `python3.6 import_script.py`

4. **Build Java application**
    - `mvn -f backend/pom.xml clean package`

5. **Run Jar file**
 - API is on `:8080/api/graphql`

6. **Start Frontend**
    - go to frontend folder
    - run `yarn` ( install dependencies )
    - run `yarn start`
    - visit `http://localhost:3000`
