#!/bin/sh
docker-compose down
mvn clean package -DskipTests
docker-compose up --build -d