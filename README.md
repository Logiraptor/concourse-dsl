# Concourse DSL

This project is an attempt to create a nicer interface for concourse using Kotlin, inspired by gradle and jenkins pipelines.


## Features

Pipelines are written as Kotlin script files. Because Kotlin is type-safe, so is your pipeline. IDE features like auto-completion or extract method work out of the box.
 

```kotlin
package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.*

// Since the pipeline is executed at generation time, it's
// easy to use a table-driven approach
val services = mapOf(
        "mailer" to "github.com/mailer.git",
        "mint" to "github.com/mint.git",
        "third" to "github.com/third.git"
)

val customPipeline = Pipeline().apply {

    for ((name, repo) in services) {
        resource(name, type = "git") {
            source = mapOf("uri" to repo, "branch" to "master")
        }
    }

    job("unit") {
        plan {
            getAllRepos() {
                trigger = true
            }

            task("test") { file = "mailer/ci/test.yml" }
        }
    }

    job("build") {
        plan {
            getAllRepos() {
                trigger = true
            }
            task("build") { file = "mailer/ci/build.yml" }
        }
    }

}

// Extending the DSL is equally easy, and works well with "Extract Function" in IDEA
private fun StepBuilder.getAllRepos(additionalConfig: Step.GetStep.() -> Unit) {
    for (name in services.keys) {
        get(name, additionalConfig)
    }
}

fun main(args: Array<String>) {
    println(generateYML(customPipeline))
}

```
Running this script will produce the following YAML
```
---
jobs:
- name: "unit"
  plan:
  - get: "mailer"
    trigger: true
  - get: "mint"
    trigger: true
  - get: "third"
    trigger: true
  - task: "test"
    file: "mailer/ci/test.yml"
- name: "build"
  plan:
  - get: "mailer"
    trigger: true
  - get: "mint"
    trigger: true
  - get: "third"
    trigger: true
  - task: "build"
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
resourceTypes: []
```

## Current Status

Right now this is just a proof of concept. There are many concourse features missing and the UX needs a lot of work. I would not recommend using this in production unless you want to take ownership.

## Design Goals

1. Consistent translation from kotlin to yml
2. Support IDE features like auto-complete and refactoring
3. Make invalid pipelines harder to write than valid pipelines
