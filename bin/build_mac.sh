#!/usr/bin/env bash
cd "$(dirname "$0")" || exit 1

cd ..

./gradlew clean build -x test
unzip app/build/distributions/app.zip -d app/build/distributions
jpackage --name Jvav \
         --module-path app/build/distributions/app/lib \
         --module jvav/jvav.App \
         --type app-image \
         --dest app/build \
         --mac-package-name Jvav \
         --mac-package-identifier jvav.app \
         --icon icons/Jvav.icns
