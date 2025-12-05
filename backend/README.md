# fpj

/bin/sh /home/misha/IdeaProjects/fpjava/restart.sh

docker logs fp_java
docker-compose up --build -d
docker-compose down
docker exec -it fp_db psql -U psqladmin -W finplan
docker exec -it fp_back_prod sh
docker logs fp_back_dev
mvn clean package -DskipTests

docker compose down --remove-orphans
docker network prune
docker container prune
