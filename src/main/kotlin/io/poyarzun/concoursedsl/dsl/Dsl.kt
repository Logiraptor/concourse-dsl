package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*

@DslMarker
annotation class ConcourseDslMarker

@ConcourseDslMarker
open class ConcourseDsl

typealias Init<T> = T.() -> Unit

fun Pipeline.job(name: String, init: Init<Job>) =
    jobs.add(Job(name).apply(init))

fun Pipeline.group(name: String, init: Init<Group>) =
    groups.add(Group(name).apply(init))

fun Pipeline.resource(name: String, type: String, init: Init<Resource>) =
    resources.add(Resource(name, type).apply(init))

fun Pipeline.resourceType(name: String, type: String, init: Init<ResourceType>) =
    resourceTypes.add(ResourceType(name, type).apply(init))


operator fun MutableList<Step>.invoke(init: Init<StepBuilder>) {
    StepBuilder(this::add).apply(init)
}

inline class StepBuilder(val addStep: (Step) -> Any?) {
    fun get(resource: String, init: Init<Step.GetStep>) =
        addStep(Step.GetStep(resource).apply(init))

    fun put(resource: String, init: Init<Step.PutStep>) =
        addStep(Step.PutStep(resource).apply(init))

    fun task(name: String, init: Init<Step.TaskStep>) =
        addStep(Step.TaskStep(name).apply(init))

    fun aggregate(init: Init<StepBuilder>) {
        val aggregateStep = Step.AggregateStep()
        StepBuilder(aggregateStep.aggregate::add).apply(init)
        addStep(aggregateStep)
    }

    fun `do`(init: Init<StepBuilder>) {
        val doStep = Step.DoStep()
        StepBuilder(doStep.steps::add).apply(init)
        addStep(doStep)
    }

    fun `try`(resource: String, init: Init<StepBuilder>) =
        OneTimeStepBuilder("try") { addStep(Step.TryStep(it)) }.apply(init)
}

fun OneTimeStepBuilder(configName: String, addStep: (Step) -> Any?): StepBuilder {
    var called: Boolean = false
    return StepBuilder {
        if (called) throw IllegalStateException("$configName may only contain at most one step")
        called = true
        addStep(it)
    }
}
