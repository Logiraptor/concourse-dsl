package io.poyarzun.concoursedsl.dsl.resources

import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.*
import io.poyarzun.concoursedsl.dsl.pipeline
import io.poyarzun.concoursedsl.resources.*
import org.junit.Test
import kotlin.test.assertEquals

class HgResourceTest {
    @Test
    fun testYaml() {
        val actualYaml = generateYML(pipeline {
            val myRepo = hgResource("my-git-resource", "git@github.com:org/repo.git") {
                source {
                    branch = "cool-branch"
                    tagFilter = "deploy\\-.*"
                }
            }

            job("get-put") {
                plan {
                    get(myRepo) {}

                    put(myRepo, "repo-path") {
                        params {
                            rebase = true
                        }
                    }
                }
            }
        })

        val expectedYaml = """
            ---
            jobs:
            - name: "get-put"
              plan:
              - get: "my-git-resource"
                params: {}
              - put: "my-git-resource"
                params:
                  repository: "repo-path"
                  rebase: true
                get_params: {}
            groups: []
            resources:
            - name: "my-git-resource"
              type: "git"
              source:
                uri: "git@github.com:org/repo.git"
                branch: "cool-branch"
                tag_filter: "deploy\\-.*"
            resource_types: []

        """.trimIndent()

        assertEquals(expectedYaml, actualYaml)
    }
}