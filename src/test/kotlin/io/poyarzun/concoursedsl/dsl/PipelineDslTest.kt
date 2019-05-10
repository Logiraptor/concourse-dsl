package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PipelineDslTest {
    @Test
    fun `dsl configures basic group properties`() {
        val result = group("All Jobs") {
            jobs.add("Job 1")
        }

        assertEquals(1, result.jobs.size)
        assertEquals("Job 1", result.jobs[0])
    }

    @Test
    fun `dsl configures basic resource type properties`() {
        val result = resourceType("email", "docker-image") {
            source {
                put("image", "somebody/email")
            }
        }

        assertEquals("email", result.name)
        assertEquals("docker-image", result.type)
        assertEquals("somebody/email", result.source["image"])
    }

    @Test
    fun `dsl configures basic resource properties`() {
        val result = resource("source-code", "git") {
            source {
                put("uri", "ssh://git@github.com/team/project.git")
                put("key", "((private_key))")
            }
        }

        result.apply {
            assertEquals("source-code", name)
            assertEquals("ssh://git@github.com/team/project.git", source["uri"])
        }
    }

    @Test
    fun `dsl configures basic job properties`() {
        val result = job("Job 1") {
            plan {
                +task("My Task") {
                    file = "source-code/unit.yml"
                }
            }
        }

        assertEquals("Job 1", result.name)

        val firstStep = result.plan[0]
        assertTrue(firstStep is Step.TaskStep)
        assertEquals("My Task", firstStep.task)
        assertEquals("source-code/unit.yml", firstStep.file)
    }
}
