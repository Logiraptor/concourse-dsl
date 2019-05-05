package io.poyarzun.concoursedsl

import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.domain.StepHookReceiver
import io.poyarzun.concoursedsl.dsl.*


val exhaustivePipeline = pipeline {
    resourceType("resource-type-name", "resource-type-type") {
        source {
            put("source-key1", "source-value1")
            put("source-key2", "source-value2")
        }
        privileged = true
        params {
            put("params-key1", "params-value1")
            put("params-key2", "params-value2")
        }
        checkEvery = "check-every-value"
        tags {
            +"tag-name1"
            +"tag-name2"
        }
    }

    resource("resource-name", "resource-type") {
        source {
            put("source-key1", "source-value1")
            put("source-key2", "source-value2")
        }
        version {
            put("version-key1", "version-value1")
            put("version-key2", "version-value2")
        }
        checkEvery = "check-every-value"
        tags {
            +"tag-name1"
            +"tag-name2"
        }
        webhookToken = "webhook-token-value"
    }

    group("group-name") {
        jobs.add("group-name-job-1")
        jobs.add("group-name-job-2")
        resources.add("group-name-resource-1")
        resources.add("group-name-resource-2")
    }

    job("job-name") {
        plan {
            exhaustiveSteps(true)
        }
        serial = true
        buildLogsToRetain = 3
        serialGroups {
            add("serial-group-name1")
            add("serial-group-name2")
        }
        maxInFlight = 7
        public = false
        disableManualTrigger = true
        interruptible = false

        onSuccess {
            exhaustiveSteps()
        }

        onFailure {
            exhaustiveSteps()
        }

        onAbort {
            exhaustiveSteps()
        }

        ensure {
            exhaustiveSteps()
        }
    }
}

private fun StepBuilder.exhaustiveSteps(recurse: Boolean = false) {
    fun StepHookReceiver.hooks() {
        if (recurse) {
            onSuccess {
                exhaustiveSteps()
            }

            onFailure {
                exhaustiveSteps()
            }

            onAbort {
                exhaustiveSteps()
            }

            ensure {
                exhaustiveSteps()
            }
        }
    }

    fun Step.baseValues() {
        tags {
            +"tag-name1"
            +"tag-name2"
        }
        timeout = "timeout-value"
        attempts = 3

        hooks()
    }

    get("get-resource-name") {
        baseValues()

        params {
            put("params-key1", "params-value1")
            put("params-key2", "params-value2")
        }
        resource = "resource-value"
        version = "version-value"
        passed {
            add("passed-1")
            add("passed-2")
        }
        trigger = true
    }

    put("put-resource-name") {
        baseValues()

        params {
            put("params-key1", "params-value1")
            put("params-key2", "params-value2")
        }
        getParams {
            put("get-params-key1", "get-params-value1")
            put("get-params-key2", "get-params-value2")
        }
        resource = "resource-value"
    }

    task("task-name") {
        inputMapping {
            put("input-mapping-key1", "input-mapping-value1")
            put("input-mapping-key2", "input-mapping-value2")
        }
        outputMapping {
            put("input-mapping-key1", "input-mapping-value1")
            put("input-mapping-key2", "input-mapping-value2")
        }

        config("platform-name") {
            run("path-value") {
                args {
                    add("arg-value1")
                    add("arg-value2")
                }
                dir = "dir-value"
                user = "user-value"
            }
            imageResource("image-resource-type") {
                source {
                    put("source-key1", "source-value1")
                    put("source-key2", "source-value2")
                }
                params {
                    put("params-key1", "params-value1")
                    put("params-key2", "params-value2")
                }
                version {
                    put("version-key1", "version-value1")
                    put("version-key2", "version-value2")
                }
            }
            rootfsUri = "rootfs-uri-value"
            input("input-name1") {
                path = "path-value"
                optional = false
            }
            input("input-name2") {
                path = "path-value"
                optional = false
            }
            output("output-name1") {
                path = "path-value"
            }
            output("output-name2") {
                path = "path-value"
            }
            cache("cache-path1")
            cache("cache-path2")

            params {
                put("params-key1", "params-value1")
                put("params-key2", "params-value2")
            }
        }

        file = "file-value"
        privileged = true
        params {
            put("params-key1", "params-value1")
            put("params-key2", "params-value2")
        }
        image = "image-value"
    }

    if (recurse) {

        aggregate {
            exhaustiveSteps()
        }

        `do` {
            exhaustiveSteps()
        }

        `try` {
            get("try-get-name") {
                baseValues()
            }
        }

    }
}

fun main() {
    println(generateYML(exhaustivePipeline))
}
