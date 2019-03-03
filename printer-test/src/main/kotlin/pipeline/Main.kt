package pipeline

import io.poyarzun.concoursedsl.dsl.cache
import io.poyarzun.concoursedsl.dsl.config
import io.poyarzun.concoursedsl.dsl.ensure
import io.poyarzun.concoursedsl.dsl.get
import io.poyarzun.concoursedsl.dsl.getParams
import io.poyarzun.concoursedsl.dsl.imageResource
import io.poyarzun.concoursedsl.dsl.input
import io.poyarzun.concoursedsl.dsl.inputMapping
import io.poyarzun.concoursedsl.dsl.inputs
import io.poyarzun.concoursedsl.dsl.job
import io.poyarzun.concoursedsl.dsl.onAbort
import io.poyarzun.concoursedsl.dsl.onFailure
import io.poyarzun.concoursedsl.dsl.onSuccess
import io.poyarzun.concoursedsl.dsl.output
import io.poyarzun.concoursedsl.dsl.outputMapping
import io.poyarzun.concoursedsl.dsl.outputs
import io.poyarzun.concoursedsl.dsl.params
import io.poyarzun.concoursedsl.dsl.passed
import io.poyarzun.concoursedsl.dsl.pipeline
import io.poyarzun.concoursedsl.dsl.plan
import io.poyarzun.concoursedsl.dsl.put
import io.poyarzun.concoursedsl.dsl.resource
import io.poyarzun.concoursedsl.dsl.serialGroups
import io.poyarzun.concoursedsl.dsl.source
import io.poyarzun.concoursedsl.dsl.tags
import io.poyarzun.concoursedsl.dsl.version

fun mainPipeline() = pipeline {
    resource("resource-name", "resource-type") {
        source {
            put("source-key1", "source-value1")
            put("source-key2", "source-value2")
        }
        checkEvery = "check-every-value"
        webhookToken = "webhook-token-value"
    }
    job("job-name") {
        buildLogsToRetain = 3
        disableManualTrigger = true
        ensure() {
            tags {
            }
        }
        interruptible = false
        maxInFlight = 7
        onAbort() {
            tags {
            }
        }
        onFailure() {
            tags {
            }
        }
        onSuccess() {
            tags {
            }
        }
        plan {
            get("get-resource-name", "{params-key1=params-value1, params-key2=params-value2}") {
                passed {
                    add("passed-1")
                    add("passed-2")
                }
                resource = "resource-value"
                trigger = true
                version = "version-value"
            }
            put("put-resource-name", "{params-key1=params-value1, params-key2=params-value2}",
                    "{get-params-key1=get-params-value1, get-params-key2=get-params-value2}") {
                resource = "resource-value"
            }
            task("task-name") {
                config("platform-name",
                        "RunConfig(path=path-value, args=[arg-value1, arg-value2], dir=dir-value, user=user-value)")
                        {
                    caches {
                        cache("cache-path1") {
                        }
                        cache("cache-path2") {
                        }
                    }
                    imageResource("image-resource-type") {
                        params {
                            put("params-key1", "params-value1")
                            put("params-key2", "params-value2")
                        }
                        source {
                            put("source-key1", "source-value1")
                            put("source-key2", "source-value2")
                        }
                        version {
                            put("version-key1", "version-value1")
                            put("version-key2", "version-value2")
                        }
                    }
                    inputs {
                        input("input-name1") {
                            optional = false
                            path = "path-value"
                        }
                        input("input-name2") {
                            optional = false
                            path = "path-value"
                        }
                    }
                    outputs {
                        output("output-name1") {
                            path = "path-value"
                        }
                        output("output-name2") {
                            path = "path-value"
                        }
                    }
                    params {
                    }
                    rootfsUri = "rootfs-uri-value"
                }
                file = "file-value"
                image = "image-value"
                inputMapping {
                    put("input-mapping-key1", "input-mapping-value1")
                    put("input-mapping-key2", "input-mapping-value2")
                }
                outputMapping {
                    put("input-mapping-key1", "input-mapping-value1")
                    put("input-mapping-key2", "input-mapping-value2")
                }
                params {
                    put("params-key1", "params-value1")
                    put("params-key2", "params-value2")
                }
                privileged = true
            }
            aggregate() {
                aggregate {
                    get("get-resource-name",
                            "{params-key1=params-value1, params-key2=params-value2}") {
                        passed {
                            add("passed-1")
                            add("passed-2")
                        }
                        resource = "resource-value"
                        trigger = true
                        version = "version-value"
                    }
                    put("put-resource-name",
                            "{params-key1=params-value1, params-key2=params-value2}",
                            "{get-params-key1=get-params-value1, get-params-key2=get-params-value2}")
                            {
                        resource = "resource-value"
                    }
                    task("task-name") {
                        config("platform-name",
                                "RunConfig(path=path-value, args=[arg-value1, arg-value2], dir=dir-value, user=user-value)")
                                {
                            caches {
                                cache("cache-path1") {
                                }
                                cache("cache-path2") {
                                }
                            }
                            imageResource("image-resource-type") {
                                params {
                                    put("params-key1", "params-value1")
                                    put("params-key2", "params-value2")
                                }
                                source {
                                    put("source-key1", "source-value1")
                                    put("source-key2", "source-value2")
                                }
                                version {
                                    put("version-key1", "version-value1")
                                    put("version-key2", "version-value2")
                                }
                            }
                            inputs {
                                input("input-name1") {
                                    optional = false
                                    path = "path-value"
                                }
                                input("input-name2") {
                                    optional = false
                                    path = "path-value"
                                }
                            }
                            outputs {
                                output("output-name1") {
                                    path = "path-value"
                                }
                                output("output-name2") {
                                    path = "path-value"
                                }
                            }
                            params {
                            }
                            rootfsUri = "rootfs-uri-value"
                        }
                        file = "file-value"
                        image = "image-value"
                        inputMapping {
                            put("input-mapping-key1", "input-mapping-value1")
                            put("input-mapping-key2", "input-mapping-value2")
                        }
                        outputMapping {
                            put("input-mapping-key1", "input-mapping-value1")
                            put("input-mapping-key2", "input-mapping-value2")
                        }
                        params {
                            put("params-key1", "params-value1")
                            put("params-key2", "params-value2")
                        }
                        privileged = true
                    }
                }
            }
            `do`() {
                do {
                    get("get-resource-name",
                            "{params-key1=params-value1, params-key2=params-value2}") {
                        passed {
                            add("passed-1")
                            add("passed-2")
                        }
                        resource = "resource-value"
                        trigger = true
                        version = "version-value"
                    }
                    put("put-resource-name",
                            "{params-key1=params-value1, params-key2=params-value2}",
                            "{get-params-key1=get-params-value1, get-params-key2=get-params-value2}")
                            {
                        resource = "resource-value"
                    }
                    task("task-name") {
                        config("platform-name",
                                "RunConfig(path=path-value, args=[arg-value1, arg-value2], dir=dir-value, user=user-value)")
                                {
                            caches {
                                cache("cache-path1") {
                                }
                                cache("cache-path2") {
                                }
                            }
                            imageResource("image-resource-type") {
                                params {
                                    put("params-key1", "params-value1")
                                    put("params-key2", "params-value2")
                                }
                                source {
                                    put("source-key1", "source-value1")
                                    put("source-key2", "source-value2")
                                }
                                version {
                                    put("version-key1", "version-value1")
                                    put("version-key2", "version-value2")
                                }
                            }
                            inputs {
                                input("input-name1") {
                                    optional = false
                                    path = "path-value"
                                }
                                input("input-name2") {
                                    optional = false
                                    path = "path-value"
                                }
                            }
                            outputs {
                                output("output-name1") {
                                    path = "path-value"
                                }
                                output("output-name2") {
                                    path = "path-value"
                                }
                            }
                            params {
                            }
                            rootfsUri = "rootfs-uri-value"
                        }
                        file = "file-value"
                        image = "image-value"
                        inputMapping {
                            put("input-mapping-key1", "input-mapping-value1")
                            put("input-mapping-key2", "input-mapping-value2")
                        }
                        outputMapping {
                            put("input-mapping-key1", "input-mapping-value1")
                            put("input-mapping-key2", "input-mapping-value2")
                        }
                        params {
                            put("params-key1", "params-value1")
                            put("params-key2", "params-value2")
                        }
                        privileged = true
                    }
                }
            }
            `try`("GetStep(get=try-get-name, params={}, resource=null, version=null, passed=null, trigger=null)")
                    {
            }
        }
        public = false
        serial = true
        serialGroups {
            add("serial-group-name1")
            add("serial-group-name2")
        }
    }
}

fun main() {
    println(mainPipeline())
}
