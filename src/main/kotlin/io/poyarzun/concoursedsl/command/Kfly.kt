package io.poyarzun.concoursedsl.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.ProcessedArgument
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.defaultLazy
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.path
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.printer.Printer
import java.io.*
import javax.script.ScriptEngineManager

class Kfly : CliktCommand() {
    companion object {
        fun main(args: Array<String>) {
            Kfly().subcommands(
                    Convert(),
                    Execute()
            ).main(args)
        }
    }

    override fun run() {
    }
}

class Convert : CliktCommand(name = "generate-kt",help = "Convert yaml to kotlin") {
    val input by argument(help = "Name of the input (.yaml) file to read").input()
    val output by option(help = "Name of the output (.kts) file to write").output()

    override fun run() {
        val yaml = input.readText()
        val dsl = Printer.convertYamlToDsl(yaml)
        output.use {
            it.write(dsl)
        }
    }
}

class Execute : CliktCommand(name = "generate-yml", help = "Convert kotlin to yaml (expects a top level function called mainPipeline which returns the pipeline object)") {
    val input by argument(help = "Name of the input (.kts) file to read").input()
    val output by option(help = "Name of the output (.yaml) file to write").output()

    override fun run() {
        val dsl = input.readText()
        val pipeline = with(ScriptEngineManager().getEngineByExtension("kts")) {
            eval(dsl)
            eval("mainPipeline()") as Pipeline
        }
        val yaml = generateYML(pipeline)
        output.use {
            it.write(yaml)
        }
    }
}

fun ProcessedArgument<String, String>.input() = convert { File(it).reader() }.defaultLazy { System.`in`.reader() }

fun NullableOption<String, String>.output() = convert { File(it).writer() }.defaultLazy { System.out.writer() }
