package pipeline

import io.poyarzun.concoursedsl.domain.aggregate
import io.poyarzun.concoursedsl.domain.`do`
import io.poyarzun.concoursedsl.domain.get
import io.poyarzun.concoursedsl.domain.group
import io.poyarzun.concoursedsl.domain.job
import io.poyarzun.concoursedsl.domain.pipeline
import io.poyarzun.concoursedsl.domain.put
import io.poyarzun.concoursedsl.domain.resource
import io.poyarzun.concoursedsl.domain.resourceType
import io.poyarzun.concoursedsl.domain.task
import io.poyarzun.concoursedsl.domain.`try`
import io.poyarzun.concoursedsl.dsl.generateYML
import java.io.File

fun mainPipeline() = pipeline {
    groups {
        +group("group-name") {
            jobs {
                +"group-name-job-1"
                +"group-name-job-2"
            }
            resources {
                +"group-name-resource-1"
                +"group-name-resource-2"
            }
        }
    }
    jobs {
        +job("job-name") {
            buildLogsToRetain = 3
            disableManualTrigger = true
            ensure = aggregate {
                +get("get-resource-name") {
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    passed {
                        +"passed-1"
                        +"passed-2"
                    }
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                    trigger = true
                    version = "version-value"
                }
                +put("put-resource-name") {
                    getParams {
                        put("get-params-key2", "get-params-value2")
                        put("get-params-key1", "get-params-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                }
                +task("task-name") {
                    config("platform-name") {
                        imageResource("image-resource-type") {
                            params {
                                put("params-key2", "params-value2")
                                put("params-key1", "params-value1")
                            }
                            source {
                                put("source-key2", "source-value2")
                                put("source-key1", "source-value1")
                            }
                            version {
                                put("version-key2", "version-value2")
                                put("version-key1", "version-value1")
                            }
                        }
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        rootfsUri = "rootfs-uri-value"
                        run("path-value") {
                            args {
                                +"arg-value1"
                                +"arg-value2"
                            }
                            dir = "dir-value"
                            user = "user-value"
                        }
                    }
                    file = "file-value"
                    image = "image-value"
                    inputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    outputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    privileged = true
                }
            }
            interruptible = false
            maxInFlight = 7
            onAbort = aggregate {
                +get("get-resource-name") {
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    passed {
                        +"passed-1"
                        +"passed-2"
                    }
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                    trigger = true
                    version = "version-value"
                }
                +put("put-resource-name") {
                    getParams {
                        put("get-params-key2", "get-params-value2")
                        put("get-params-key1", "get-params-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                }
                +task("task-name") {
                    config("platform-name") {
                        imageResource("image-resource-type") {
                            params {
                                put("params-key2", "params-value2")
                                put("params-key1", "params-value1")
                            }
                            source {
                                put("source-key2", "source-value2")
                                put("source-key1", "source-value1")
                            }
                            version {
                                put("version-key2", "version-value2")
                                put("version-key1", "version-value1")
                            }
                        }
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        rootfsUri = "rootfs-uri-value"
                        run("path-value") {
                            args {
                                +"arg-value1"
                                +"arg-value2"
                            }
                            dir = "dir-value"
                            user = "user-value"
                        }
                    }
                    file = "file-value"
                    image = "image-value"
                    inputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    outputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    privileged = true
                }
            }
            onFailure = aggregate {
                +get("get-resource-name") {
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    passed {
                        +"passed-1"
                        +"passed-2"
                    }
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                    trigger = true
                    version = "version-value"
                }
                +put("put-resource-name") {
                    getParams {
                        put("get-params-key2", "get-params-value2")
                        put("get-params-key1", "get-params-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                }
                +task("task-name") {
                    config("platform-name") {
                        imageResource("image-resource-type") {
                            params {
                                put("params-key2", "params-value2")
                                put("params-key1", "params-value1")
                            }
                            source {
                                put("source-key2", "source-value2")
                                put("source-key1", "source-value1")
                            }
                            version {
                                put("version-key2", "version-value2")
                                put("version-key1", "version-value1")
                            }
                        }
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        rootfsUri = "rootfs-uri-value"
                        run("path-value") {
                            args {
                                +"arg-value1"
                                +"arg-value2"
                            }
                            dir = "dir-value"
                            user = "user-value"
                        }
                    }
                    file = "file-value"
                    image = "image-value"
                    inputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    outputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    privileged = true
                }
            }
            onSuccess = aggregate {
                +get("get-resource-name") {
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    passed {
                        +"passed-1"
                        +"passed-2"
                    }
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                    trigger = true
                    version = "version-value"
                }
                +put("put-resource-name") {
                    getParams {
                        put("get-params-key2", "get-params-value2")
                        put("get-params-key1", "get-params-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    attempts = 3
                    resource = "resource-value"
                    tags {
                        +"tag-name1"
                        +"tag-name2"
                    }
                    timeout = "timeout-value"
                }
                +task("task-name") {
                    config("platform-name") {
                        imageResource("image-resource-type") {
                            params {
                                put("params-key2", "params-value2")
                                put("params-key1", "params-value1")
                            }
                            source {
                                put("source-key2", "source-value2")
                                put("source-key1", "source-value1")
                            }
                            version {
                                put("version-key2", "version-value2")
                                put("version-key1", "version-value1")
                            }
                        }
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        rootfsUri = "rootfs-uri-value"
                        run("path-value") {
                            args {
                                +"arg-value1"
                                +"arg-value2"
                            }
                            dir = "dir-value"
                            user = "user-value"
                        }
                    }
                    file = "file-value"
                    image = "image-value"
                    inputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    outputMapping {
                        put("input-mapping-key2", "input-mapping-value2")
                        put("input-mapping-key1", "input-mapping-value1")
                    }
                    params {
                        put("params-key2", "params-value2")
                        put("params-key1", "params-value1")
                    }
                    privileged = true
                }
            }
            plan {
                +aggregate {
                    +get("get-resource-name") {
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        attempts = 3
                        ensure = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onAbort = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onFailure = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onSuccess = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        passed {
                            +"passed-1"
                            +"passed-2"
                        }
                        resource = "resource-value"
                        tags {
                            +"tag-name1"
                            +"tag-name2"
                        }
                        timeout = "timeout-value"
                        trigger = true
                        version = "version-value"
                    }
                    +put("put-resource-name") {
                        getParams {
                            put("get-params-key2", "get-params-value2")
                            put("get-params-key1", "get-params-value1")
                        }
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        attempts = 3
                        ensure = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onAbort = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onFailure = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onSuccess = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        resource = "resource-value"
                        tags {
                            +"tag-name1"
                            +"tag-name2"
                        }
                        timeout = "timeout-value"
                    }
                    +task("task-name") {
                        config("platform-name") {
                            imageResource("image-resource-type") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                source {
                                    put("source-key2", "source-value2")
                                    put("source-key1", "source-value1")
                                }
                                version {
                                    put("version-key2", "version-value2")
                                    put("version-key1", "version-value1")
                                }
                            }
                            params {
                                put("params-key2", "params-value2")
                                put("params-key1", "params-value1")
                            }
                            rootfsUri = "rootfs-uri-value"
                            run("path-value") {
                                args {
                                    +"arg-value1"
                                    +"arg-value2"
                                }
                                dir = "dir-value"
                                user = "user-value"
                            }
                        }
                        file = "file-value"
                        image = "image-value"
                        inputMapping {
                            put("input-mapping-key2", "input-mapping-value2")
                            put("input-mapping-key1", "input-mapping-value1")
                        }
                        outputMapping {
                            put("input-mapping-key2", "input-mapping-value2")
                            put("input-mapping-key1", "input-mapping-value1")
                        }
                        params {
                            put("params-key2", "params-value2")
                            put("params-key1", "params-value1")
                        }
                        privileged = true
                    }
                    +aggregate {
                        +aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                    }
                    +`do` {
                        +aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                    }
                    +`try`(get("try-get-name") {
                        attempts = 3
                        ensure = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onAbort = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onFailure = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        onSuccess = aggregate {
                            +get("get-resource-name") {
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                passed {
                                    +"passed-1"
                                    +"passed-2"
                                }
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                                trigger = true
                                version = "version-value"
                            }
                            +put("put-resource-name") {
                                getParams {
                                    put("get-params-key2", "get-params-value2")
                                    put("get-params-key1", "get-params-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                attempts = 3
                                resource = "resource-value"
                                tags {
                                    +"tag-name1"
                                    +"tag-name2"
                                }
                                timeout = "timeout-value"
                            }
                            +task("task-name") {
                                config("platform-name") {
                                    imageResource("image-resource-type") {
                                        params {
                                            put("params-key2", "params-value2")
                                            put("params-key1", "params-value1")
                                        }
                                        source {
                                            put("source-key2", "source-value2")
                                            put("source-key1", "source-value1")
                                        }
                                        version {
                                            put("version-key2", "version-value2")
                                            put("version-key1", "version-value1")
                                        }
                                    }
                                    params {
                                        put("params-key2", "params-value2")
                                        put("params-key1", "params-value1")
                                    }
                                    rootfsUri = "rootfs-uri-value"
                                    run("path-value") {
                                        args {
                                            +"arg-value1"
                                            +"arg-value2"
                                        }
                                        dir = "dir-value"
                                        user = "user-value"
                                    }
                                }
                                file = "file-value"
                                image = "image-value"
                                inputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                outputMapping {
                                    put("input-mapping-key2", "input-mapping-value2")
                                    put("input-mapping-key1", "input-mapping-value1")
                                }
                                params {
                                    put("params-key2", "params-value2")
                                    put("params-key1", "params-value1")
                                }
                                privileged = true
                            }
                        }
                        tags {
                            +"tag-name1"
                            +"tag-name2"
                        }
                        timeout = "timeout-value"
                    }
                    )}
            }
            public = false
            serial = true
            serialGroups {
                +"serial-group-name1"
                +"serial-group-name2"
            }
        }
    }
    resourceTypes {
        +resourceType("resource-type-name", "resource-type-type") {
            checkEvery = "check-every-value"
            params {
                put("params-key2", "params-value2")
                put("params-key1", "params-value1")
            }
            privileged = true
            source {
                put("source-key2", "source-value2")
                put("source-key1", "source-value1")
            }
            tags {
                +"tag-name1"
                +"tag-name2"
            }
        }
    }
    resources {
        +resource("resource-name", "resource-type") {
            source {
                put("source-key2", "source-value2")
                put("source-key1", "source-value1")
            }
            checkEvery = "check-every-value"
            tags {
                +"tag-name1"
                +"tag-name2"
            }
            version {
                put("version-key2", "version-value2")
                put("version-key1", "version-value1")
            }
            webhookToken = "webhook-token-value"
        }
    }
}


fun main() {
    val yml = generateYML(mainPipeline())
    println(yml)
    val yamlFile = File("printer-test/src/main/kotlin/actual.yml")
    yamlFile.writeText(yml)
}
