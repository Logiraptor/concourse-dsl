package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Step
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PipelineDslTest {
    private val pipeline = Pipeline()

    @Test
    fun `dsl configures basic group properties`() {
        pipeline.apply {
            group("All Jobs") {
                jobs.add("Job 1")
            }
        }

        pipeline.groups[0].apply {
            assertEquals(1, jobs.size)
            assertEquals("Job 1", jobs[0])
        }
    }

    @Test
    fun `dsl configures basic resource type properties`() {
        pipeline.apply {
            resourceType("email", "docker-image") {
                source = mapOf("image" to "somebody/email")
            }
        }

        pipeline.resourceTypes[0].apply {
            assertEquals("email", name)
            assertEquals("docker-image", type)
            val source = source
            assertTrue(source != null)
            assertEquals("somebody/email", source["image"])
        }
    }

    @Test
    fun `dsl configures basic resource properties`() {
        pipeline.apply {
            resource("source-code", "git") {
                source = mapOf(
                    "uri" to "ssh://git@github.com/team/project.git",
                    "key" to "((private_key))"
                )
            }
        }

        pipeline.resources[0].apply {
            assertEquals("source-code", name)
            val source = source
            assertTrue(source != null)
            assertEquals("ssh://git@github.com/team/project.git", source["uri"])
        }
    }

    @Test
    fun `dsl configures basic job properties`() {
        pipeline.apply {
            job("Job 1") {
                plan {
                    task("My Task") {
                        file = "source-code/unit.yml"
                    }
                }
            }
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
