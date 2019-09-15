package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.resources.*

val nonProdCF = cfResource("non-prod") {
    source("https://api.sys.dev.cf.io", "dev", "dev") {
    }
}

val prodCF = cfResource("prod") {
    source("https://api.sys.prod.cf.io", "prod", "prod") {
    }
}

val dslSourceCode = gitResource("concourse-dsl") {
    source("git@github.com:Logiraptor/concourse-dsl.git") {
        branch = "master"
    }
}

val semverPipeline = pipeline {
    resources {
        +semverResource("iam-s3-resource-tile-version") {
            source(SemverResource.S3, "eagle-ci-blobs", "iam-s3-resource-tile-version", "{{aws-access-key-id}}", "{{aws-secret-access-key}}") {
                initialVersion = "0.0.25"
            }
            checkEvery = ""
            webhookToken = ""
        }
        +resource("ci-docker-image", "docker-image") {
            source {
                this["password"] = "{{dockerhub-password}}"
            }
            checkEvery = ""
            webhookToken = ""
        }
    }
}

val customPipeline = pipeline {
    resources {
        +nonProdCF
        +prodCF

        +dslSourceCode
    }

    resourceTypes {
        +resourceType("email", "") {

        }
    }

    sharedTemplate("mailer")
    sharedTemplate("mint")
    sharedTemplate("third")

    groups {
        +group("All") {
            this@pipeline.jobs.forEach { job ->
                jobs.add(job.name)
            }

            this@pipeline.resources.forEach { resource: Resource<*> ->
                resources.add(resource.name)
            }
        }
    }

    groups {
        +group("Mint") {
            jobs.add("MINT Test & Staging Deploy")
            jobs.add("MINT Prod Deploy")
        }
    }
}

private fun Pipeline.sharedTemplate(name: String) {
    val sourceCodeResource = gitResource("$name-source-code") {
        source("https://github.com/$name.git") {
            branch = "master"
        }
    }

    resources {
        +sourceCodeResource
    }

    jobs {
        +job("${name.toUpperCase()} Test & Staging Deploy") {
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

        +job("${name.toUpperCase()} Prod Deploy") {
            plan {
                +get(sourceCodeResource) {
                    trigger = false
                    passed {
                        +"${name.toUpperCase()} Test & Staging Deploy"
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

                    +`try` { get(sourceCodeResource) {} }
                }
            }
        }
    }
}

fun main() {
    println(generateYML(customPipeline))
}
