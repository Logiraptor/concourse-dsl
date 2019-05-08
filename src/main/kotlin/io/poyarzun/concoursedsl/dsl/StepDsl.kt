package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Step

typealias Source = DslMap<String, Any?>
typealias Params = DslMap<String, Any?>
typealias Version = DslMap<String, String>

typealias Tags = DslList<String>

fun `try`(step: Step) = Step.TryStep(step)

fun `do`(configBlock: ConfigBlock<DslList<Step>>) = Step.DoStep().apply {
    `do`.apply(configBlock)
}

fun aggregate(configBlock: ConfigBlock<DslList<Step>>) = Step.AggregateStep().apply {
    aggregate.apply(configBlock)
}

fun task(name: String, configBlock: ConfigBlock<Step.TaskStep>) =
        Step.TaskStep(name).apply(configBlock)
