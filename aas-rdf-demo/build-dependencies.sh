#!/usr/bin/env bash

set -e

# check for dependency
DEPENDENCY="at.srfg.iasset:asset-connector:0.2.0"
if mvn -q dependency:get -Dartifact=${DEPENDENCY} -Dtransitive=false; then
  echo "${DEPENDENCY} found in mvn repository"
else
  echo "${DEPENDENCY} not found - building it"
  cd target
  git clone https://github.com/i-Asset/asset-repository.git
  cd asset-repository
  mvn clean install -DskipTests
  echo "SUCCESS: asset-repository build finished without error"
fi
