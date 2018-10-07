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
