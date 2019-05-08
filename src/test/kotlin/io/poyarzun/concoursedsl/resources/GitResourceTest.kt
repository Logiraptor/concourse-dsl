package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.job
import io.poyarzun.concoursedsl.dsl.pipeline
import org.junit.Test
import kotlin.test.assertEquals

class GitResourceTest {
    @Test
    fun testYaml() {
        val actualYaml = generateYML(pipeline {
            val myRepo = gitResource("my-git-resource") {
                source("git@github.com:org/repo.git") {
                    branch = "cool-branch"
                    gitConfig {
                        +GitResource.Config("custom-git", "custom-git-value")
                    }
                    httpsTunnel("proxy.org", "8080") {
                        proxyUser = "admin"
                        proxyPassword = "password"
                    }
                }
            }

            resources(myRepo)

            job("get-put-repo") {
                plan {
                    +get(myRepo) {
                        params {
                            submodules = "all"
                            depth = 10
                        }
                    }

                    +put(myRepo) {
                        params("./output") {
                            rebase = true
                        }

                        getParams {
                            depth = 1
                        }
                    }
                }
            }
        })

        val expectedYaml = """
            ---
            jobs:
            - name: "get-put-repo"
              plan:
              - get: "my-git-resource"
                params:
                  depth: 10
                  submodules: "all"
              - put: "my-git-resource"
                params:
                  repository: "./output"
                  rebase: true
                get_params:
                  depth: 1
            groups: []
            resources:
            - name: "my-git-resource"
              type: "git"
              source:
                uri: "git@github.com:org/repo.git"
                branch: "cool-branch"
                git_config:
                - name: "custom-git"
                  value: "custom-git-value"
                https_tunnel:
                  proxy_host: "proxy.org"
                  proxy_port: "8080"
                  proxy_user: "admin"
                  proxy_password: "password"
            resource_types: []

        """.trimIndent()

        assertEquals(expectedYaml, actualYaml)
    }
}