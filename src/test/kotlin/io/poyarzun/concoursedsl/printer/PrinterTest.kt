package io.poyarzun.concoursedsl.printer

import org.junit.Test
import kotlin.test.assertEquals


class PrinterTest {
    @Test
    fun testBasicDsl() {
        val testYaml = """
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
                non-string: true
                non-bool: 4
                uri: "git@github.com:Logiraptor/concourse-dsl"
                private_key: "((github-deploy-key))"
                nested:
                - "value1"
                - "value2"
                other_nested:
                  foo: "bar"
                  null_value:
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

        val dsl = Printer.convertYamlToDsl(testYaml)

        val expectedDsl = """
                package pipeline

                import io.poyarzun.concoursedsl.domain.`do`
                import io.poyarzun.concoursedsl.domain.`try`
                import io.poyarzun.concoursedsl.domain.aggregate
                import io.poyarzun.concoursedsl.domain.cache
                import io.poyarzun.concoursedsl.domain.get
                import io.poyarzun.concoursedsl.domain.group
                import io.poyarzun.concoursedsl.domain.input
                import io.poyarzun.concoursedsl.domain.job
                import io.poyarzun.concoursedsl.domain.output
                import io.poyarzun.concoursedsl.domain.pipeline
                import io.poyarzun.concoursedsl.domain.put
                import io.poyarzun.concoursedsl.domain.resource
                import io.poyarzun.concoursedsl.domain.resourceType
                import io.poyarzun.concoursedsl.domain.task
                import io.poyarzun.concoursedsl.dsl.generateYML

                fun Test() = job("Test") {
                  buildLogsToRetain = 1
                  disableManualTrigger = false
                  maxInFlight = 1
                  onAbort = get("yet-another-resource") {
                  }
                  onFailure = get("some-other-resource") {
                  }
                  onSuccess = get("some-resource") {
                  }
                  plan {
                    +get("concourse-dsl-source") {
                      trigger = true
                    }
                    +task("run-tests") {
                      config("linux") {
                        caches {
                          +cache("concourse-dsl-source") {
                          }
                        }
                        imageResource("docker-image") {
                          source {
                            this["resource"] = "maven"
                          }
                        }
                        inputs {
                          +input("concourse-dsl-source") {
                          }
                        }
                        outputs {
                          +output("concourse-dsl-source") {
                          }
                        }
                        rootfsUri = "not-a-real-value"
                        run("/bin/sh") {
                          args {
                            +"-c"
                            +""${'"'}
                                |cd source-code
                                |./gradlew test
                                |mkdir result
                                |echo "OK" > result/result.out
                                ""${'"'}.trimMargin()
                          }
                        }
                      }
                      inputMapping {
                        this["source-code"] = "concourse-dsl-source"
                      }
                      outputMapping {
                        this["result"] = "output"
                      }
                    }
                    +put("results") {
                      getParams {
                        this["skip_download"] = "true"
                      }
                    }
                  }
                  serialGroups {
                    +"unique-jobs"
                  }
                }


                fun concourse_dsl_source() = resource("concourse-dsl-source", "git") {
                  source {
                    this["other_nested"] = mapOf("foo" to "bar", "null_value" to null)
                    this["nested"] = listOf("value1", "value2")
                    this["private_key"] = "((github-deploy-key))"
                    this["uri"] = "git@github.com:Logiraptor/concourse-dsl"
                    this["non-bool"] = 4
                    this["non-string"] = true
                  }
                  checkEvery = "20m"
                  webhookToken = "totally-a-secret"
                }


                fun results() = resource("results", "s3") {
                  source {
                    this["secret_key"] = "((aws_secret_key))"
                    this["access_key"] = "((aws_access_key))"
                    this["bucket"] = "results-bucket"
                  }
                  checkEvery = ""
                  webhookToken = ""
                }


                fun rss() = resourceType("rss", "docker-image") {
                  checkEvery = "10m"
                  source {
                    this["tag"] = "latest"
                    this["repository"] = "suhlig/concourse-rss-resource"
                  }
                }


                fun mainPipeline() = pipeline {
                  jobs {
                    +Test()
                  }
                  resources {
                    +concourse_dsl_source()
                    +results()
                  }
                  resourceTypes {
                    +rss()
                  }
                  groups {
                    +group("Source Code") {
                      resources {
                        +"concourse-dsl-source"
                      }
                    }
                  }
                }

                fun main() {
                  println(generateYML(mainPipeline()))
                }
                
        """.trimIndent()

        assertEquals(expectedDsl, dsl)
    }
}