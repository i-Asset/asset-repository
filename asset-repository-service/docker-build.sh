#!/usr/bin/env bash

set -e

source .env

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Version from pom.xml: ${VERSION}"

# check for dependency
DEPENDENCY="at.srfg.iasset:asset-repository-service:${VERSION}"
if ! mvn -q dependency:get -Dartifact=${DEPENDENCY} -Dtransitive=false; then echo "${DEPENDENCY} not found"; exit 1; fi
echo "${DEPENDENCY} found"
CI_COMMIT_REF_NAME=$(git rev-parse --abbrev-ref HEAD)
echo "Commit Reference: ${CI_COMMIT_REF_NAME}"
CI_COMMIT_SHORT_SHA=$(git rev-parse --short HEAD)
echo "Commit Short SHA: ${CI_COMMIT_SHORT_SHA}"
CI_COMMIT_BRANCH=${CI_COMMIT_REF_NAME}
echo "Commit Branch: ${CI_COMMIT_BRANCH}"
CI_COMMIT_TAG=$(git tag --points-at HEAD)
CI_COMMIT_TAG="${CI_COMMIT_TAG:-untagged}"
echo "Commit Tag: ${CI_COMMIT_TAG}"


if [ -z ${DOCKER_REPO+x} ]; then
  echo "DOCKER_REPO not set, exiting"
  exit 1
fi

docker build -t "${APP_PATH}" \
 -t "${DOCKER_REPO}/${APP_PATH}" \
 -t "${DOCKER_REPO}/${APP_PATH}:${VERSION}" \
 -t "${DOCKER_REPO}/${APP_PATH}:${CI_COMMIT_SHORT_SHA}" \
 -t "${DOCKER_REPO}/${APP_PATH}:${CI_COMMIT_TAG}" \
 -t "${DOCKER_REPO}/${APP_PATH}:${CI_COMMIT_BRANCH}" \
 -t "${DOCKER_REPO}/${APP_PATH}:${CI_COMMIT_REF_NAME}" .
