package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Task
import io.poyarzun.concoursedsl.resources.get
import io.poyarzun.concoursedsl.resources.gitResource
import org.junit.Test
import kotlin.test.assertEquals

class YamlTest {
    @Test
    fun testBasicPipelineYamlUsesSnakeCase() {
        val pipeline = pipeline {
            resourceType("rss", "docker-image") {
                source = mapOf(
                        "repository" to "suhlig/concourse-rss-resource",
                        "tag" to "latest"
                )
                checkEvery = "10m"
            }

            val sourceCode = gitResource("concourse-dsl-source", "git@github.com:Logiraptor/concourse-dsl") {
                source {
                    privateKey = "((github-deploy-key))"
                }
                checkEvery = "20m"
                webhookToken = "totally-a-secret"
            }

            resource("results", "s3") {
                source {
                    put("bucket", "results-bucket")
                    put("access_key", "((aws_access_key))")
                    put("secret_key", "((aws_secret_key))")
                }
            }

            job("Test") {
                plan {
                    get(sourceCode) {
                        trigger = true
                    }
                    task("run-tests") {
                        inputMapping = mapOf(
                                "source-code" to "concourse-dsl-source"
                        )
                        outputMapping = mapOf(
                                "result" to "output"
                        )
                        config = Task(
                                rootfsUri = "not-a-real-value",
                                platform = "linux",
                                imageResource = Task.Resource(
                                        type = "docker-image",
                                        source = mapOf(
                                                "resource" to "maven"
                                        )
                                ),
                                run = Task.RunConfig("/bin/sh", args = mutableListOf("-c", """
                        cd source-code
                        ./gradlew test
                        mkdir result
                        echo "OK" > result/result.out
                    """.trimIndent())),
                                inputs = mutableListOf(Task.Input("concourse-dsl-source"))
                        )
                    }
                    put("results") {
                        getParams = mutableMapOf(
                                "skip_download" to "true"
                        )
                    }
                }

                buildLogsToRetain = 1
                serialGroups = mutableListOf("unique-jobs")
                maxInFlight = 1

                disableManualTrigger = false

                onSuccess {
                    get("some-resource") {}
                }
                onFailure {
                    get("some-other-resource") {}
                }
                onAbort {
                    get("yet-another-resource") {}
                }
            }
        }

        val yaml = generateYML(pipeline)

        val expectedYaml = """
            ---
            jobs:
            - name: "Test"
              plan:
              - get: "concourse-dsl-source"
                params: {}
                trigger: true
              - task: "run-tests"
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
                input_mapping:
                  source-code: "concourse-dsl-source"
                output_mapping:
                  result: "output"
              - put: "results"
                params: {}
                get_params:
                  skip_download: "true"
              build_logs_to_retain: 1
              serial_groups:
              - "unique-jobs"
              max_in_flight: 1
              disable_manual_trigger: false
              on_success:
                get: "some-resource"
                params: {}
              on_failure:
                get: "some-other-resource"
                params: {}
              on_abort:
                get: "yet-another-resource"
                params: {}
            groups: []
            resources:
            - name: "concourse-dsl-source"
              type: "git"
              source:
                uri: "git@github.com:Logiraptor/concourse-dsl"
                private_key: "((github-deploy-key))"
              check_every: "20m"
              webhook_token: "totally-a-secret"
            - name: "results"
              type: "s3"
              source:
                bucket: "results-bucket"
                access_key: "((aws_access_key))"
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