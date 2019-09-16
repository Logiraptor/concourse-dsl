@file:DependsOn("io.poyarzun:concourse-dsl:0.7.0")

import java.io.File
import io.poyarzun.concoursedsl.printer.Printer
import io.poyarzun.concoursedsl.domain.pipeline
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.command.Kfly

fun main(args: Array<String>) {
    Kfly.main(args)
}

