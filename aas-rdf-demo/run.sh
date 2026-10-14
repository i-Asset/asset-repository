#!/usr/bin/env bash

set -e

source .env

if [ -z ${GIT_USER+x} ]; then
  echo "GIT_USER not set, exiting"
  exit 1
fi

if [ -z ${GIT_SERVER+x} ]; then
  echo "GIT_SERVER not set, exiting"
  exit 1
fi

if [ -z ${GIT_TOKEN+x} ]; then
  echo "GIT_TOKEN not set, exiting"
  exit 1
else
  echo "trying to login to container registry as ${GIT_USER}"
  docker login "${GIT_SERVER}" -u "${GIT_USER}" -p "${GIT_TOKEN}"
fi

docker compose -f docker-compose.yml up -d
