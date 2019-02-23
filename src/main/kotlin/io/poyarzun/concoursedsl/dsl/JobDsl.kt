package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Job
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step

fun Job.plan(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder(this.plan::add).apply(configBlock)
}

fun <SourceProps> Resource<SourceProps>.source(configBlock: ConfigBlock<SourceProps>) {
    this.source.apply(configBlock)
}

fun <InProps> Step.GetStep<InProps>.params(configBlock: ConfigBlock<InProps>) {
    this.params.apply(configBlock)
}

fun <InProps, OutProps> Step.PutStep<InProps, OutProps>.params(configBlock: ConfigBlock<OutProps>) {
    this.params.apply(configBlock)
}

fun <InProps, OutProps> Step.PutStep<InProps, OutProps>.getParams(configBlock: ConfigBlock<InProps>) {
    this.getParams.apply(configBlock)
}
