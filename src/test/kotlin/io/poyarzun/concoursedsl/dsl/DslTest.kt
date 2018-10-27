package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Step
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DslTest {
    private val pipeline = Pipeline()

    @Test
    fun `dsl configures basic properties`() {
        pipeline.apply {
            resource("source-code", "git") {
                source = mapOf(
                    "uri" to "ssh://git@github.com/team/project.git",
                    "key" to "((private_key))"
                )
            }

            job("Job 1") {
                plan {
                    task("My Task") {
                        file = "source-code/unit.yml"
                    }
                }
            }
        }

        pipeline.resources[0].apply {
            assertEquals("source-code", name)
            val source = source
            assertTrue(source != null)
            assertEquals("ssh://git@github.com/team/project.git", source["uri"])
        }

        pipeline.jobs[0].apply {
            assertEquals("Job 1", name)

            val firstStep = plan[0]
            assertTrue(firstStep is Step.TaskStep)
            assertEquals("My Task", firstStep.task)
            assertEquals("source-code/unit.yml", firstStep.file)
        }
    }
}
