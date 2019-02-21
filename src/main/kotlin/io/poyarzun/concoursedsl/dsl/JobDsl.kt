package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Job
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step

fun Job.plan(init: Init<StepBuilder>) {
    StepBuilder(this.plan::add).apply(init)
}

fun <SourceProps> Resource<SourceProps>.source(init: Init<SourceProps>) {
    this.source.apply(init)
}

fun <InProps> Step.GetStep<InProps>.params(init: Init<InProps>) {
    this.params.apply(init)
}

fun <InProps, OutProps> Step.PutStep<InProps, OutProps>.params(init: Init<OutProps>) {
    this.params.apply(init)
}

fun <InProps, OutProps> Step.PutStep<InProps, OutProps>.getParams(init: Init<InProps>) {
    this.getParams.apply(init)
}
