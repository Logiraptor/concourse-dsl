package io.poyarzun.concoursedsl.e2e

import fr.xgouchet.elmyr.junit.JUnitForger
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.printer.Printer
import org.jetbrains.kotlin.utils.rethrow
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import kotlin.test.Test
import kotlin.test.assertEquals


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

    @Before
    fun setUp() {
        Tests.scriptEngine.setBindings(Tests.scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE)
    }

    @Test
    fun testDslRoundTrip() {
        val pipeline = PipelineGenerator(forger).generateRandomPipeline()
        val yaml = generateYML(pipeline)
        val dsl = Printer().convertYamlToDsl(yaml)
        try {
            Tests.scriptEngine.eval(dsl)
            val resultPipeline = Tests.scriptEngine.eval("mainPipeline()") as Pipeline
            val resultYaml = generateYML(resultPipeline)
            val resultDsl = Printer().convertYamlToDsl(resultYaml)

            assertEquals(yaml + dsl, resultYaml + resultDsl)
        } catch (e: ScriptException) {
            println(yaml)
            println(dsl)
            rethrow(e)
        }
    }
}

object Tests {
    val scriptEngine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
}
