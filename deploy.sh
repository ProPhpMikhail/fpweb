#!/usr/bin/env bash
set -e

PROJECT_DIR="/var/www/finplan"
BRANCH="main"

cd "$PROJECT_DIR"

echo ">>> git pull"
git fetch origin "$BRANCH"
git reset --hard "origin/$BRANCH"

echo ">>> docker up"
#docker stop $(docker ps -a -q)
#docker rm $(docker ps -a -q)
#docker compose down
docker compose --profile prod up --build -d

echo ">>> docker clear old images"
docker image prune -f

echo ">>> done"
