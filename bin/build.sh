#!/usr/bin/env bash
cd "$(dirname "$0")" || exit 1

cd ..

./gradlew clean build -x test
