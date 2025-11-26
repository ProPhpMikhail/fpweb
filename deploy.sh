#!/usr/bin/env bash
set -e

PROJECT_DIR="/var/www/finplan"
BRANCH="main"

cd "$PROJECT_DIR"

echo ">>> git pull"
git fetch origin "$BRANCH"
git reset --hard "origin/$BRANCH"

echo ">>> docker up"
docker compose up -d --build db backend frontend-prod

echo ">>> docker clear old images"
docker image prune -f

echo ">>> done"
