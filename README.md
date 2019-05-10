# Concourse DSL

This project is an attempt to create a nicer interface for concourse using Kotlin, inspired by gradle and jenkins pipelines.


## Features

Pipelines are written as Kotlin script files. Because Kotlin is type-safe, so is your pipeline. IDE features like auto-completion or extract method work out of the box.

```kotlin
package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.dsl.*
import io.poyarzun.concoursedsl.resources.GitResource
import io.poyarzun.concoursedsl.resources.get
import io.poyarzun.concoursedsl.resources.gitResource

// Since the pipeline is executed at generation time, it's
// easy to use a table-driven approach
val services = mapOf(
        "mailer" to "github.com/mailer.git",
        "mint" to "github.com/mint.git",
        "third" to "github.com/third.git"
)

val customPipeline = pipeline {
    val repos = mutableListOf<GitResource>()
    resources {
        for ((name, repo) in services) {
            val repository = gitResource(name) {
                source(repo) {
                    branch = "master"
                }
            }
            +repository
            repos.add(repository)
        }
    }

    jobs {
        +job("unit") {
            plan {
                getAllRepos(repos) {
                    trigger = true
                }
                +task("unit") { file = "mailer/ci/test.yml" }
            }
        }

        +job("build") {
            plan {
                getAllRepos(repos) {
                    trigger = true
                    passed("unit")
                }
                +task("unit") { file = "mailer/ci/build.yml" }
            }
        }
    }
}

// Extending the DSL is equally easy, and works well with "Extract Function" in IDEA
private fun DslList<Step>.getAllRepos(repos: List<GitResource>, additionalConfig: GitResource.GetStep.() -> Unit) {
    +aggregate {
        for (repo in repos) +get(repo, additionalConfig)
    }
}

fun main() {
    println(generateYML(customPipeline))
}
```

The script above prints:

```yaml
---
jobs:
- name: "unit"
  plan:
  - aggregate:
    - trigger: true
      get: "mailer"
    - trigger: true
      get: "mint"
    - trigger: true
      get: "third"
  - task: "unit"
    file: "mailer/ci/test.yml"
- name: "build"
  plan:
  - aggregate:
    - passed:
      - "unit"
      trigger: true
      get: "mailer"
    - passed:
      - "unit"
      trigger: true
      get: "mint"
    - passed:
      - "unit"
      trigger: true
      get: "third"
  - task: "unit"
    file: "mailer/ci/build.yml"
groups: []
resources:
- name: "mailer"
  type: "git"
  source:
    uri: "github.com/mailer.git"
    branch: "master"
- name: "mint"
  type: "git"
  source:
    uri: "github.com/mint.git"
    branch: "master"
- name: "third"
  type: "git"
  source:
    uri: "github.com/third.git"
    branch: "master"
resource_types: []
```

## Current Status

Right now this is a proof of concept. There are a couple concourse features missing. I would not recommend using this in production unless you want to take ownership.

## Design Goals

1. Consistent translation from Kotlin to YAML
2. Support IDE features like auto-complete and refactoring
3. Make invalid pipelines harder to write than valid pipelines

## Converting your pipeline to kotlin

Generally there are rules for how code is converted to the DSL.

1. Yaml objects and arrays become kotlin lambdas
2. Yaml key: value pairs become kotlin property assignments
