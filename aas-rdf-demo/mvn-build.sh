#!/usr/bin/env bash

set -e

# check for dependency
DEPENDENCY="at.srfg.iasset:asset-connector:0.2.0"
if ! mvn -q dependency:get -Dartifact=${DEPENDENCY} -Dtransitive=false; then echo "${DEPENDENCY} not found"; exit 1; fi

if ! mvn clean install -DskipTests; then exit 1; fi

echo "SUCCESS: mvn build (skipped tests) finished without error"
