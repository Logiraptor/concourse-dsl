#!/usr/bin/env bash

function start_concourse {
    if [[ ! -d concourse-docker ]]; then
        git clone https://github.com/concourse/concourse-docker.git
    fi

    pushd concourse-docker
        ./keys/generate
        docker-compose up -d
    popd
}

function stop_concourse {
    pushd concourse-docker
        docker-compose down
    popd
}

start_concourse

docker build -t concourse-dsl-test .

docker run --network concourse-docker_default -i concourse-dsl-test /bin/bash <<'EOF'
    set -ex

    source ~/.sdkman/bin/sdkman-init.sh

    while ! fly -t ci login -c http://web:8080 -u test -p test > /dev/null 2>&1; do
        echo "Waiting for concourse to start"
        sleep 1
    done

    fly -t ci set-pipeline \
        --non-interactive \
        --pipeline main \
        --config <(kscript pipeline.kts)

    fly -t ci unpause-pipeline \
        --pipeline main

    build_id=$(fly -t ci trigger-job \
                --job main/main |
                awk '{print $3}' |
                tr -d '#')

    fly -t ci watch \
        --build ${build_id}
EOF

stop_concourse
