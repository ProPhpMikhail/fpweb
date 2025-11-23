#!/bin/sh
set -e
cd ..
docker compose down
docker compose up -d --build frontend