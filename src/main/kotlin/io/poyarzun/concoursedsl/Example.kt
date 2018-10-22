package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.dsl.*

val customPipeline = pipeline {
    resource("non-prod", "cf") {
        source = mapOf("api" to "https://api.sys.dev.cf.io", "space" to "dev")
    }

    resource("prod", "cf") {
        source = mapOf("api" to "https://api.sys.cf.io", "space" to "dev")
    }

    sharedTemplate("mailer")
    sharedTemplate("mint")
    sharedTemplate("third")
}

private fun ConfigWrapper.sharedTemplate(name: String) {
    val sourceCodeResource = "$name-source-code"

    resource(sourceCodeResource, "git") {
        source = mapOf("uri" to "https://github.com/$name.git", "branch" to "master")
    }

    job("${name.toUpperCase()} Test & Staging Deploy") {
        plan {
            get(sourceCodeResource) {
                trigger = true
            }
            task("test") {
                file = "tasks/build.yml"
            }
            put("non-prod") {

            }
        }
    }

    job("${name.toUpperCase()} Prod Deploy") {
        plan {
            get(sourceCodeResource) {
                trigger = false
                passed = listOf("${name.toUpperCase()} Test & Staging Deploy")
            }
            task("build") {
                file = "tasks/build.yml"
            }
            put("prod") {

            }
        }
    }
}

fun main(args: Array<String>) {
    println(customPipeline.generateYML())
}
