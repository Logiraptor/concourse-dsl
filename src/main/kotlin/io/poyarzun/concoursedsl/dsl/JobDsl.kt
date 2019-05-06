package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Job
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.domain.Task

fun Job.plan(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder(this.plan::add).apply(configBlock)
}

fun <SourceProps : Any> Resource<SourceProps>.source(configBlock: ConfigBlock<SourceProps>) {
    this.source.apply(configBlock)
}

fun <InProps : Any> Step.GetStep<InProps>.params(configBlock: ConfigBlock<InProps>) {
    this.params.apply(configBlock)
}

fun <InProps : Any, OutProps : Any> Step.PutStep<InProps, OutProps>.params(configBlock: ConfigBlock<OutProps>) {
    this.params.apply(configBlock)
}

fun <InProps : Any, OutProps : Any> Step.PutStep<InProps, OutProps>.getParams(configBlock: ConfigBlock<InProps>) {
    this.getParams.apply(configBlock)
}

fun Task.input(name: String, configBlock: ConfigBlock<Task.Input>) {
    inputs.add(Task.Input(name).apply(configBlock))
}

fun Task.output(name: String, configBlock: ConfigBlock<Task.Output>) {
    outputs.add(Task.Output(name).apply(configBlock))
}

fun Task.cache(name: String) {
    caches.add(Task.Cache(name))
}
