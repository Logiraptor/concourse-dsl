package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.dsl.*

val customPipeline = Pipeline().apply {
    resource("non-prod", "cf") {
        source = mapOf("api" to "https://api.sys.dev.cf.io", "space" to "dev")
    }

    resource("prod", "cf") {
        source = mapOf("api" to "https://api.sys.cf.io", "space" to "dev")
    }

    resourceType("email", "") {

    }

    sharedTemplate("mailer")
    sharedTemplate("mint")
    sharedTemplate("third")

    group("All") {
        this@apply.jobs.forEach { job ->
            jobs.add(job.name)
        }

        this@apply.resources.forEach { resource ->
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
            aggregate {
                get(sourceCodeResource) {
                    trigger = true
                }

                put("prod") {

                }
            }
        }
    }
}

fun main(args: Array<String>) {
    println(customPipeline.generateYML())
}
