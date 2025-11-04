#!/usr/bin/env bash

set -e

source .env

if [ -z ${DOCKER_REPO+x} ]; then
  echo "DOCKER_REPO not set, exiting"
  exit 1
fi

if [ -z ${DOCKER_USER+x} ]; then
  echo "DOCKER_USER not set, exiting"
  exit 1
fi

if [ -z ${DOCKER_TOKEN+x} ]; then
  echo "DOCKER_TOKEN not set, exiting"
  exit 1
else
  echo "trying to login to container registry as ${DOCKER_USER}"
  docker login -u "${DOCKER_USER}" -p "${DOCKER_TOKEN}"
  echo "trying to push the container"
  docker --debug push -a "${DOCKER_REPO}/${APP_PATH}"
fi
