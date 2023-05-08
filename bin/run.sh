#!/usr/bin/env bash
cd "$(dirname "$0")" || exit 1

cd ..

./gradlew clean build -x test
unzip app/build/distributions/app.zip -d app/build/distributions
./app/build/distributions/app/bin/app
