package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.dsl.`try`
import io.poyarzun.concoursedsl.dsl.aggregate
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.task
import io.poyarzun.concoursedsl.resources.gitResource

val customPipeline = pipeline {
    resource("non-prod", "cf") {
        source {
            put("api", "https://api.sys.dev.cf.io")
            put("space", "dev")
        }
    }

    resource("prod", "cf") {
        source {
            put("api", "https://api.sys.cf.io")
            put("space", "dev")
        }
    }

    gitResource("prod") {
        source("git@github.com:Logiraptor/concourse-dsl.git") {
            branch = "master"
        }
    }

    resourceType("email", "") {

    }

    sharedTemplate("mailer")
    sharedTemplate("mint")
    sharedTemplate("third")

    group("All") {
        this@pipeline.jobs.forEach { job ->
            jobs.add(job.name)
        }

        this@pipeline.resources.forEach { resource: Resource<*> ->
            resources.add(resource.name)
        }
    }

    group("Mint") {
        jobs.add("MINT Test & Staging Deploy")
        jobs.add("MINT Prod Deploy")
    }

}

private fun Pipeline.sharedTemplate(name: String) {
    val sourceCodeResource = "$name-source-code"

    gitResource(sourceCodeResource) {
        source("https://github.com/$name.git") {
            branch = "master"
        }
    }

    job("${name.toUpperCase()} Test & Staging Deploy") {
        plan {
            +get(sourceCodeResource) {
                trigger = true
            }
            +task("test") {
                file = "tasks/build.yml"
            }
            +put("non-prod") {

            }
        }
    }

    job("${name.toUpperCase()} Prod Deploy") {
        plan {
            +get(sourceCodeResource) {
                trigger = false
                passed {
                    add("${name.toUpperCase()} Test & Staging Deploy")
                }
            }
            +task("build") {
                file = "tasks/build.yml"
            }
            +put("prod") {

            }
            +aggregate {
                +get(sourceCodeResource) {
                    trigger = true
                }

                +put("prod") {

                }

                +`try`(get(sourceCodeResource) {})
            }
        }
    }
}

fun main(args: Array<String>) {
    println(generateYML(customPipeline))
}
