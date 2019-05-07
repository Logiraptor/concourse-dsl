package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
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
                source {
                    put("image", "somebody/email")
                }
            }
        }

        pipeline.resourceTypes[0].apply {
            assertEquals("email", name)
            assertEquals("docker-image", type)
            assertEquals("somebody/email", source["image"])
        }
    }

    @Test
    fun `dsl configures basic resource properties`() {
        pipeline.resources {
            +resource("source-code", "git") {
                source {
                    put("uri", "ssh://git@github.com/team/project.git")
                    put("key", "((private_key))")
                }
            }
        }

        (pipeline.resources[0] as Resource<Source>).apply {
            assertEquals("source-code", name)
            assertEquals("ssh://git@github.com/team/project.git", source["uri"])
        }
    }

    @Test
    fun `dsl configures basic job properties`() {
        pipeline.apply {
            job("Job 1") {
                plan {
                    +task("My Task") {
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
