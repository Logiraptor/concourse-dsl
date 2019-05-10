package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StepDslTest {
    @Test
    fun `get dsl configures basic get step properties`() {
        val result = get("source-code") {
            passed {
                add("Unit Tests")
                add("QA Deployment")
            }
        }

        assertEquals("source-code", result.get)
        result.passed.let {
            assertEquals("Unit Tests", it[0])
            assertEquals("QA Deployment", it[1])
        }
    }

    @Test
    fun `put dsl configures basic put step properties`() {
        val result = put("cf") {
            params {
                put("manifest", "source-code/manifest.yml")
            }
        }

        assertEquals("cf", result.put)
        assertEquals("source-code/manifest.yml", result.params["manifest"])
    }

    @Test
    fun `task dsl configures basic task step properties`() {
        val result = task("unit tests") {
            file = "source-code/unit.yml"
        }

        assertEquals("unit tests", result.task)
        assertEquals("source-code/unit.yml", result.file)
    }

    @Test
    fun `aggregate dsl collects steps into an aggregate`() {
        val result = aggregate {
            +task("unit tests") {
                file = "source-code/unit.yml"
            }
        }

        result.aggregate[0].let { task ->
            assertTrue(task is Step.TaskStep)
            assertEquals("unit tests", task.task)
            assertEquals("source-code/unit.yml", task.file)
        }
    }

    @Test
    fun `do dsl collects steps into a do step`() {
        val result = `do` {
            +task("unit tests") {
                file = "source-code/unit.yml"
            }
        }

        result.`do`[0].let { task ->
            assertTrue(task is Step.TaskStep)
            assertEquals("unit tests", task.task)
            assertEquals("source-code/unit.yml", task.file)
        }
    }

    @Test
    fun `try dsl collects one step into a try step`() {
        val result = `try`(task("unit tests") {
            file = "source-code/unit.yml"
        })

        result.`try`.let { task ->
            assertTrue(task is Step.TaskStep)
            assertEquals("unit tests", task.task)
            assertEquals("source-code/unit.yml", task.file)
        }
    }
}
