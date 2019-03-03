#!/usr/bin/env bash

set -ex

pushd $HOME/workspace/concourse-dsl/
    ./gradlew run
    pushd printer-test
        docker build -t printer-test .
        docker run --rm -v $HOME/.gradle/:/root/.gradle -v $PWD:/project -w /project printer-test ./gradlew --no-daemon run
    popd
popd

