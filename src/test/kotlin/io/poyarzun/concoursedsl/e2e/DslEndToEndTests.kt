package io.poyarzun.concoursedsl.e2e

import fr.xgouchet.elmyr.junit.JUnitForger
import fr.xgouchet.elmyr.junit.Repeat
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.readYML
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import fr.xgouchet.elmyr.junit.Repeater
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.printer.Printer
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import javax.script.ScriptEngineManager


@RunWith(Parameterized::class)
class DslEndToEndTests {
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
    fun testDslRoundTrip() {
        val pipeline = PipelineGenerator(forger).generateRandomPipeline()
        val yaml = generateYML(pipeline)
        val dsl = Printer.convertYamlToDsl(yaml)
        val resultPipeline = with(ScriptEngineManager().getEngineByExtension("kts")) {
            eval(dsl)
            eval("mainPipeline()") as Pipeline
        }
        val resultYaml = generateYML(resultPipeline)

        assertEquals(yaml, resultYaml)
    }
}