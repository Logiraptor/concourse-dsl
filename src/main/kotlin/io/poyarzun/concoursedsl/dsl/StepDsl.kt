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

fun <InProps : Any> baseGet(resource: String, inProps: InProps, configBlock: ConfigBlock<Step.GetStep<InProps>>) =
        Step.GetStep(resource, inProps).apply(configBlock)

fun <InProps : Any, OutProps : Any> basePut(resource: String, outProps: OutProps, inProps: InProps, configBlock: ConfigBlock<Step.PutStep<InProps, OutProps>>) =
        Step.PutStep(resource, outProps, inProps).apply(configBlock)

fun task(name: String, configBlock: ConfigBlock<Step.TaskStep>) =
        Step.TaskStep(name).apply(configBlock)

fun get(resource: String, configBlock: ConfigBlock<Step.GetStep<Params>>) =
        baseGet(resource, DslMap.empty(), configBlock)

fun put(resource: String, configBlock: ConfigBlock<Step.PutStep<Params, Params>>) =
        basePut(resource, DslMap.empty(), DslMap.empty(), configBlock)
