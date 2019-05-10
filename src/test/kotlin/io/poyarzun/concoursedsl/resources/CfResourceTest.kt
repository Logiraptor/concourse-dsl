package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.job
import io.poyarzun.concoursedsl.domain.pipeline
import io.poyarzun.concoursedsl.dsl.generateYML
import org.junit.Test
import kotlin.test.assertEquals

class CfResourceTest {

    @Test
    fun completeYaml() {

        val actualYaml = generateYML(pipeline {
            val resource = cfResource("resource-deploy-web-app") {
                source("https://api.run.pivotal.io", "ORG", "SPACE") {
                    username = "EMAIL"
                    password = "PASSWORD"
                    skipCertCheck = false
                    clientId = "client-id"
                    clientSecret = "1Ur7Nyq4CtROxW9q4MUr"
                    verbose = true
                }
            }

            resources(resource)

            jobs {
                +job("job-deploy-app") {
                    plan {
                        +put(resource) {
                            params("build-output/manifest.yml") {
                                path = "/build/lib/my-springboot-app.jar"
                                currentAppName = "turquoise-app"
                                vars = mapOf(
                                        "alpha" to "apple",
                                        "beta" to "banana"
                                )
                                varsFiles = listOf("mu.properties", "nu.properties")
                                dockerPassword = "gibberish"
                                dockerUserName = "simon"
                                showAppLog = true
                                noStart = false
                                environmentVariables =
                                        mapOf(
                                                "key" to "value",
                                                "key2" to "value2")
                            }
                        }
                    }
                }
            }

        })

        val expectedYaml = """
            ---
            jobs:
            - name: "job-deploy-app"
              plan:
              - put: "resource-deploy-web-app"
                params:
                  manifest: "build-output/manifest.yml"
                  path: "/build/lib/my-springboot-app.jar"
                  current_app_name: "turquoise-app"
                  environment_variables:
                    key: "value"
                    key2: "value2"
                  vars:
                    alpha: "apple"
                    beta: "banana"
                  vars_files:
                  - "mu.properties"
                  - "nu.properties"
                  docker_user_name: "simon"
                  docker_password: "gibberish"
                  show_app_log: true
                  no_start: false
            groups: []
            resources:
            - name: "resource-deploy-web-app"
              type: "cf"
              source:
                api: "https://api.run.pivotal.io"
                organization: "ORG"
                space: "SPACE"
                username: "EMAIL"
                password: "PASSWORD"
                client_id: "client-id"
                client_secret: "1Ur7Nyq4CtROxW9q4MUr"
                skip_cert_check: false
                verbose: true
            resource_types: []

            """.trimIndent()

        assertEquals(expectedYaml, actualYaml)

    }

    @Test
    fun testDocumentYaml() {

        // recreates the sample from this page
        // https://github.com/concourse/cf-resource

        val actualYaml = generateYML(pipeline {
            val resource = cfResource("resource-deploy-web-app") {
                source("https://api.run.pivotal.io", "ORG", "SPACE") {
                    username = "EMAIL"
                    password = "PASSWORD"
                    skipCertCheck = false
                }
            }

            resources(resource)

            jobs {
                +job("job-deploy-app") {
                    plan {
                        +put(resource) {
                            params("build-output/manifest.yml") {
                                environmentVariables =
                                        mapOf(
                                                "key" to "value",
                                                "key2" to "value2")
                            }
                        }
                    }
                }
            }
        })

        val expectedYaml = """
            ---
            jobs:
            - name: "job-deploy-app"
              plan:
              - put: "resource-deploy-web-app"
                params:
                  manifest: "build-output/manifest.yml"
                  environment_variables:
                    key: "value"
                    key2: "value2"
            groups: []
            resources:
            - name: "resource-deploy-web-app"
              type: "cf"
              source:
                api: "https://api.run.pivotal.io"
                organization: "ORG"
                space: "SPACE"
                username: "EMAIL"
                password: "PASSWORD"
                skip_cert_check: false
            resource_types: []

            """
                .trimIndent()


        assertEquals(expectedYaml, actualYaml)

    }

}