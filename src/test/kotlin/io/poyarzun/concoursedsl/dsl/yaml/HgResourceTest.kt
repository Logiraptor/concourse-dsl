package io.poyarzun.concoursedsl.dsl.yaml

import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.pipeline
import io.poyarzun.concoursedsl.resources.GitSourceProps
import io.poyarzun.concoursedsl.resources.gitResource
import io.poyarzun.concoursedsl.resources.hgResource
import org.junit.Test
import kotlin.test.assertEquals

class HgResourceTest {
    @Test
    fun testYaml() {
        val actualYaml = generateYML(pipeline {
            hgResource("my-git-resource", "git@github.com:org/repo.git") {
                source {
                    branch = "cool-branch"
                    tagFilter = "deploy\\-.*"
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
                tag_filter: "deploy\\-.*"
            resource_types: []

        """.trimIndent()

        assertEquals(expectedYaml, actualYaml)
    }
}