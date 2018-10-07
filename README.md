# Concourse DSL

This project is an attempt to create a nicer interface for concourse using Kotlin, inspired by gradle and jenkins pipelines.


## Features

Pipelines are written as Kotlin script files. Because Kotlin is type-safe, so is your pipeline. IDE features like auto-completion or extract method work out of the box.
 

```kotlin

package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.PlanWrapper
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.pipeline
import io.poyarzun.concoursedsl.dsl.plan

// Since the pipeline is executed at generation time, it's
// easy to use a table-driven approach
val services = mapOf(
    "mailer" to "github.com/mailer.git",
    "mint" to "github.com/mint.git",
    "third" to "github.com/third.git"
)

val customPipeline = pipeline {
    for ((name, repo) in services) {
        resource(name, type = "git") {
            source = mapOf("uri" to repo, "branch" to "master")
        }
    }

    job("unit") {
        plan {
            getAllRepos { trigger = true }
            task("unit") { file = "mailer/ci/test.yml" }
        }
    }

    job("build") {
        plan {
            getAllRepos {
                trigger = true
                passed = listOf("unit")
            }
            task("unit") { file = "mailer/ci/build.yml" }
        }
    }
}

// Extending the DSL is equally easy, and works well with "Extract Function" in IDEA
private fun PlanWrapper.getAllRepos(additionalConfig: Step.GetStep.() -> Unit) {
    for (name in services.keys) get(name, additionalConfig)
}

fun main(args: Array<String>) {
    println(customPipeline.generateYML())
}

```

## Current Status

Right now this is just a proof of concept. There are many concourse features missing and the UX needs a lot of work. I would not recommend using this in production unless you want to take ownership.

