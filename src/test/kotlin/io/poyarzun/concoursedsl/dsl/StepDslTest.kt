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

    @Test
    fun `aggregate dsl collects steps into an aggregate`() {
        val result = testPlan {
            aggregate {
                task("unit tests") {
                    file = "source-code/unit.yml"
                }
            }
        }

        result.let { step ->
            assertTrue(step is Step.AggregateStep)
            step.aggregate[0].let { task ->
                assertTrue(task is Step.TaskStep)
                assertEquals("unit tests", task.task)
                assertEquals("source-code/unit.yml", task.file)
            }
        }
    }

    @Test
    fun `do dsl collects steps into a do step`() {
        val result = testPlan {
            `do` {
                task("unit tests") {
                    file = "source-code/unit.yml"
                }
            }
        }

        result.let { step ->
            assertTrue(step is Step.DoStep)
            step.`do`[0].let { task ->
                assertTrue(task is Step.TaskStep)
                assertEquals("unit tests", task.task)
                assertEquals("source-code/unit.yml", task.file)
            }
        }
    }

    @Test
    fun `try dsl collects one step into a try step`() {
        val result = testPlan {
            `try` {
                task("unit tests") {
                    file = "source-code/unit.yml"
                }
            }
        }

        result.let { step ->
            assertTrue(step is Step.TryStep)
            step.`try`.let { task ->
                assertTrue(task is Step.TaskStep)
                assertEquals("unit tests", task.task)
                assertEquals("source-code/unit.yml", task.file)
            }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `try dsl throws with more than one step`() {
        testPlan {
            `try` {
                task("unit tests") {
                    file = "source-code/unit.yml"
                }

                put("cf-prod") {}
            }
        }
    }
}

