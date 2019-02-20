package io.poyarzun.concoursedsl.dsl.yaml

import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.pipeline
import io.poyarzun.concoursedsl.resources.GitSourceProps
import io.poyarzun.concoursedsl.resources.gitResource
import org.junit.Test
import kotlin.test.assertEquals

class GitResourceTest {
    @Test
    fun testYaml() {
        val actualYaml = generateYML(pipeline {
            gitResource("my-git-resource", "git@github.com:org/repo.git") {
                source {
                    branch = "cool-branch"
                    gitConfig = mutableListOf(
                            GitSourceProps.GitConfig("custom-git", "custom-git-value")
                    )
                    httpsTunnel = GitSourceProps.HttpProxy(
                            proxyHost = "proxy.org",
                            proxyPort = "8080",
                            proxyUser = "admin",
                            proxyPassword = "password"
                    )
                }
            }
        })

        val expectedYaml = """
            ---
            jobs: []
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