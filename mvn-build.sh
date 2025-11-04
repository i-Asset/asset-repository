#!/usr/bin/env bash

set -e


if ! mvn clean install -DskipTests; then exit 1; fi

echo "SUCCESS: mvn build (skipped tests) finished without error"
