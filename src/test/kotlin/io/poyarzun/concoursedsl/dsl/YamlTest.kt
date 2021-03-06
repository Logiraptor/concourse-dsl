package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.resources.get
import io.poyarzun.concoursedsl.resources.gitResource
import org.junit.Test
import kotlin.test.assertEquals

class YamlTest {
    @Test
    fun testBasicPipelineYamlUsesSnakeCase() {
        val pipeline = pipeline {
            resourceTypes {
                +resourceType("rss", "docker-image") {
                    source {
                        this["repository"] = "suhlig/concourse-rss-resource"
                        this["tag"] = "latest"
                    }
                    checkEvery = "10m"
                }
            }

            val sourceCode = gitResource("concourse-dsl-source") {
                source("git@github.com:Logiraptor/concourse-dsl") {
                    privateKey = "((github-deploy-key))"
                }
                checkEvery = "20m"
                webhookToken = "totally-a-secret"
            }

            groups {
                +group("Source Code") {
                    resources {
                        +sourceCode.name
                    }
                }
            }

            resources {
                +sourceCode

                +resource("results", "s3") {
                    source {
                        this["bucket"] = "results-bucket"
                        this["access_key"] = "((aws_access_key))"
                        this["secret_key"] = "((aws_secret_key))"
                    }
                }
            }

            jobs {
                +job("Test") {
                    plan {
                        +get(sourceCode) {
                            trigger = true
                        }
                        +task("run-tests") {
                            inputMapping {
                                put("source-code", "concourse-dsl-source")
                            }
                            outputMapping {
                                put("result", "output")
                            }
                            config("linux") {
                                rootfsUri = "not-a-real-value"
                                imageResource("docker-image") {
                                    source {
                                        put("resource", "maven")
                                    }
                                }
                                run("/bin/sh") {
                                    args("-c", """
                                        cd source-code
                                        ./gradlew test
                                        mkdir result
                                        echo "OK" > result/result.out
                                    """.trimIndent()
                                    )
                                    inputs {
                                        +input("concourse-dsl-source") {}
                                    }
                                    outputs {
                                        +output("concourse-dsl-source") {}
                                    }
                                    caches {
                                        +cache("concourse-dsl-source") {}
                                    }
                                }
                            }
                        }
                        +put("results") {
                            getParams {
                                put("skip_download", "true")
                            }
                        }
                    }

                    buildLogsToRetain = 1
                    serialGroups("unique-jobs")
                    maxInFlight = 1

                    disableManualTrigger = false

                    onSuccess = get("some-resource") {}
                    onFailure = get("some-other-resource") {}
                    onAbort = get("yet-another-resource") {}
                }
            }
        }

        val yaml = generateYML(pipeline)

        val expectedYaml = """
            ---
            jobs:
            - name: "Test"
              plan:
              - trigger: true
                get: "concourse-dsl-source"
              - task: "run-tests"
                input_mapping:
                  source-code: "concourse-dsl-source"
                output_mapping:
                  result: "output"
                config:
                  platform: "linux"
                  run:
                    path: "/bin/sh"
                    args:
                    - "-c"
                    - "cd source-code\n./gradlew test\nmkdir result\necho \"OK\" > result/result.out"
                  image_resource:
                    type: "docker-image"
                    source:
                      resource: "maven"
                  rootfs_uri: "not-a-real-value"
                  inputs:
                  - name: "concourse-dsl-source"
                  outputs:
                  - name: "concourse-dsl-source"
                  caches:
                  - path: "concourse-dsl-source"
              - put: "results"
                get_params:
                  skip_download: "true"
              build_logs_to_retain: 1
              serial_groups:
              - "unique-jobs"
              max_in_flight: 1
              disable_manual_trigger: false
              on_success:
                get: "some-resource"
              on_failure:
                get: "some-other-resource"
              on_abort:
                get: "yet-another-resource"
            groups:
            - name: "Source Code"
              jobs: []
              resources:
              - "concourse-dsl-source"
            resources:
            - check_every: "20m"
              webhook_token: "totally-a-secret"
              name: "concourse-dsl-source"
              type: "git"
              source:
                uri: "git@github.com:Logiraptor/concourse-dsl"
                private_key: "((github-deploy-key))"
            - name: "results"
              type: "s3"
              source:
                access_key: "((aws_access_key))"
                bucket: "results-bucket"
                secret_key: "((aws_secret_key))"
            resource_types:
            - name: "rss"
              type: "docker-image"
              source:
                repository: "suhlig/concourse-rss-resource"
                tag: "latest"
              check_every: "10m"

        """.trimIndent()

        assertEquals(expectedYaml, yaml)
    }
}