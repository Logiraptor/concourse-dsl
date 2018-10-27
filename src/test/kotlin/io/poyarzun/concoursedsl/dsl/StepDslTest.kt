package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Step
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StepDslTest {

    private fun testPlan(init: Init<StepBuilder>): Step {
        var step: Step? = null
        StepBuilder {
            step = it
            null
        }.apply(init)
        return step!!
    }

    @Test
    fun `get dsl configures basic get step properties`() {
        val result = testPlan {
            get("source-code") {
                passed = listOf("Unit Tests", "QA Deployment")
            }
        }

        result.let { step ->
            assertTrue(step is Step.GetStep)
            assertEquals("source-code", step.get)

            step.passed.let {
                assertTrue(it != null)
                assertEquals("Unit Tests", it[0])
                assertEquals("QA Deployment", it[1])
            }
        }
    }

    @Test
    fun `put dsl configures basic put step properties`() {
        val result = testPlan {
            put("cf") {
                params = mapOf("manifest" to "source-code/manifest.yml")
            }
        }

        result.let { step ->
            assertTrue(step is Step.PutStep)
            assertEquals("cf", step.put)

            step.params.let {
                assertTrue(it != null)
                assertEquals("source-code/manifest.yml", it["manifest"])
            }
        }
    }

    @Test
    fun `task dsl configures basic task step properties`() {
        val result = testPlan {
            task("unit tests") {
                file = "source-code/unit.yml"
            }
        }

        result.let { step ->
            assertTrue(step is Step.TaskStep)
            assertEquals("unit tests", step.task)

            assertEquals("source-code/unit.yml", step.file)
        }
    }
}

