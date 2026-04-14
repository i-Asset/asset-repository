#!/usr/bin/env bash

set -e

if ! ./build-dependencies.sh; then exit 1; fi
if ! ./mvn-build.sh; then exit 1; fi
if ! ./docker-build.sh; then exit 1; fi
if ! ./docker-push.sh; then exit 1; fi
