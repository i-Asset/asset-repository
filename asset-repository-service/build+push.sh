#!/usr/bin/env bash

set -e

if ! ./docker-build.sh; then exit 1; fi
if ! ./docker-push.sh; then exit 1; fi
