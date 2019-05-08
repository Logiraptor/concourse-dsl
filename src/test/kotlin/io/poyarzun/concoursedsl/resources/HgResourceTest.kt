package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.job
import io.poyarzun.concoursedsl.dsl.pipeline
import org.junit.Test
import kotlin.test.assertEquals

class HgResourceTest {
    @Test
    fun testYaml() {
        val actualYaml = generateYML(pipeline {
            val myRepo = hgResource("my-hg-resource") {
                source("git@github.com:org/repo.git") {
                    branch = "cool-branch"
                    tagFilter = "deploy\\-.*"
                }
            }

            resources(myRepo)

            job("get-put") {
                plan {
                    +get(myRepo) {}

                    +put(myRepo) {
                        params("repo-path") {
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
              - get: "my-hg-resource"
              - put: "my-hg-resource"
                params:
                  repository: "repo-path"
                  rebase: true
            groups: []
            resources:
            - name: "my-hg-resource"
              type: "hg"
              source:
                uri: "git@github.com:org/repo.git"
                branch: "cool-branch"
                tag_filter: "deploy\\-.*"
            resource_types: []

        """.trimIndent()

        assertEquals(expectedYaml, actualYaml)
    }
}