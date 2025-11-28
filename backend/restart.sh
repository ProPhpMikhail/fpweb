#!/bin/sh
set -e
#mvn clean package -DskipTests
cd ..
docker compose up --build db backend-dev