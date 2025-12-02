#!/bin/sh
set -e
cd ..
docker compose --profile dev up --build db backend-dev