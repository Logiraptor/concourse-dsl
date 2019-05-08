package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.job
import io.poyarzun.concoursedsl.dsl.pipeline
import org.junit.Test
import kotlin.test.assertEquals

class TimeResourceTest {
    @Test
    fun testYaml() {
        val actualYaml = generateYML(pipeline {
            val myTime = timeResource("every-10") {
                source {
                    interval = "10m"
                }
            }

            resources(myTime)

            job("get-put") {
                plan {
                    +get(myTime)

                    +put(myTime)
                }
            }
        })

        val expectedYaml = """
            ---
            jobs:
            - name: "get-put"
              plan:
              - get: "every-10"
              - put: "every-10"
            groups: []
            resources:
            - name: "every-10"
              type: "time"
              source:
                interval: "10m"
            resource_types: []

        """.trimIndent()

        assertEquals(expectedYaml, actualYaml)
    }
}