up-elastic:
	docker-compose up -d elasticsearch

up-postgres:
	docker-compose up -d postgres

up-backend:
	make up-elastic
	make up-postgres
	mvn -f backend/pom.xml clean package;
	docker-compose up --build backend

up-frontend:
	yarn --cwd frontend install
	yarn --cwd frontend build
	docker-compose up --build frontend

