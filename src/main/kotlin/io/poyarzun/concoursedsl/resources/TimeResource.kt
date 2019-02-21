package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.Init
import io.poyarzun.concoursedsl.dsl.StepBuilder
import io.poyarzun.concoursedsl.dsl.baseResource

class TimeSourceProps(
        var interval: String? = null,
        var location: String? = null,
        var start: String? = null,
        var stop: String? = null,
        var days: String? = null
)

class TimeInProps

class TimeOutProps

fun Pipeline.timeResource(name: String, init: Init<Resource<TimeSourceProps>>) =
        this.baseResource(name, "time", TimeSourceProps(), init)

fun StepBuilder.get(repo: Resource<TimeSourceProps>, init: Init<Step.GetStep<TimeInProps>>) =
        this.baseGet(repo.name, TimeInProps(), init)

fun StepBuilder.put(repo: Resource<TimeSourceProps>, init: Init<Step.PutStep<TimeInProps, TimeOutProps>>) =
        this.basePut(repo.name, TimeOutProps(), TimeInProps(), init)
