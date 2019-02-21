package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.domain.StepHookReceiver

typealias Object = MutableMap<String, Any?>

fun Step.tags(vararg tags: String) {
    this.tags = mutableListOf(*tags)
}

fun StepHookReceiver.onSuccess(init: Init<StepBuilder>) {
    StepBuilder {
        this.onSuccess = it
        null
    }.apply(init)
}

fun StepHookReceiver.onFailure(init: Init<StepBuilder>) {
    StepBuilder {
        this.onFailure = it
        null
    }.apply(init)
}

fun StepHookReceiver.onAbort(init: Init<StepBuilder>) {
    StepBuilder {
        this.onAbort = it
        null
    }.apply(init)
}

fun StepHookReceiver.ensure(init: Init<StepBuilder>) {
    StepBuilder {
        this.ensure = it
        null
    }.apply(init)
}

class StepBuilder(val addStep: (Step) -> Any?) {
    fun <InProps> baseGet(resource: String, inProps: InProps, init: Init<Step.GetStep<InProps>>) =
            addStep(Step.GetStep(resource, inProps).apply(init))

    fun <InProps, OutProps> basePut(resource: String, outProps: OutProps, inProps: InProps, init: Init<Step.PutStep<InProps, OutProps>>) =
            addStep(Step.PutStep(resource, outProps, inProps).apply(init))

    fun task(name: String, init: Init<Step.TaskStep>) =
            addStep(Step.TaskStep(name).apply(init))

    fun aggregate(init: Init<StepBuilder>) {
        val aggregateStep = Step.AggregateStep()
        StepBuilder(aggregateStep.aggregate::add).apply(init)
        addStep(aggregateStep)
    }

    fun `do`(init: Init<StepBuilder>) {
        val doStep = Step.DoStep()
        StepBuilder(doStep.`do`::add).apply(init)
        addStep(doStep)
    }

    fun `try`(init: Init<StepBuilder>) =
            oneTimeStepBuilder("try") { addStep(Step.TryStep(it)) }.apply(init)

    private fun oneTimeStepBuilder(configName: String, addStep: (Step) -> Any?): StepBuilder {
        var called = false
        return StepBuilder {
            if (called) throw IllegalStateException("$configName may only contain at most one step")
            called = true
            addStep(it)
        }
    }
}

fun StepBuilder.get(resource: String, init: Init<Step.GetStep<Object>>) =
        baseGet(resource, mutableMapOf(), init)

fun StepBuilder.put(resource: String, init: Init<Step.PutStep<Object, Object>>) =
        basePut(resource, mutableMapOf(), mutableMapOf(), init)
