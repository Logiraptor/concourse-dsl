@file:DependsOn("io.poyarzun:concourse-dsl:0.1.1")
@file:DependsOn("com.fasterxml.jackson.core:jackson-databind:2.9.5")
@file:DependsOn("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.5")
@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.5")

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.dsl.*

val customPipeline = pipeline {
    job("main") {
        plan {
            task("say-hello") {
                config("linux", "echo") {
                    imageResource("docker-image") {
                        source {
                            put("repository", "alpine")
                        }
                    }

                    run {
                        args {
                            add("Hello, World!")
                        }
                    }
                }
            }
        }
    }
}

println(generateYML(customPipeline))
