package io.poyarzun.concoursedsl.e2e

import fr.xgouchet.elmyr.junit.JUnitForger
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.readYML
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals


@RunWith(Parameterized::class)
class YamlEndToEndTests {
    @get:Rule
    val forger: JUnitForger = JUnitForger()

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return mutableListOf<Array<Any>>().apply {
                repeat(100) { add(arrayOf()) }
            }
        }
    }

    @Test
    fun testYMLRoundTrip() {
        val pipeline = PipelineGenerator(forger).generateRandomPipeline()
        val yaml = generateYML(pipeline)
        val resultPipeline = readYML(yaml)
        val resultYaml = generateYML(resultPipeline)

        assertEquals(yaml, resultYaml)
    }
}