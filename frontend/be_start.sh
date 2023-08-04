cd ..
docker-compose up -d elasticsearch
docker-compose up -d postgres
mvn -f backend/pom.xml clean package
docker-compose up --build backend
