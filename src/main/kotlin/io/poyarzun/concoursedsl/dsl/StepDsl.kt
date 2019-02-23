package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.domain.StepHookReceiver

typealias Object = MutableMap<String, Any?>

fun Step.tags(vararg tags: String) {
    this.tags = mutableListOf(*tags)
}

fun StepHookReceiver.onSuccess(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder {
        this.onSuccess = it
        null
    }.apply(configBlock)
}

fun StepHookReceiver.onFailure(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder {
        this.onFailure = it
        null
    }.apply(configBlock)
}

fun StepHookReceiver.onAbort(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder {
        this.onAbort = it
        null
    }.apply(configBlock)
}

fun StepHookReceiver.ensure(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder {
        this.ensure = it
        null
    }.apply(configBlock)
}

class StepBuilder(val addStep: (Step) -> Any?) {
    fun <InProps> baseGet(resource: String, inProps: InProps, configBlock: ConfigBlock<Step.GetStep<InProps>>) =
            addStep(Step.GetStep(resource, inProps).apply(configBlock))

    fun <InProps, OutProps> basePut(resource: String, outProps: OutProps, inProps: InProps, configBlock: ConfigBlock<Step.PutStep<InProps, OutProps>>) =
            addStep(Step.PutStep(resource, outProps, inProps).apply(configBlock))

    fun task(name: String, configBlock: ConfigBlock<Step.TaskStep>) =
            addStep(Step.TaskStep(name).apply(configBlock))

    fun aggregate(configBlock: ConfigBlock<StepBuilder>) {
        val aggregateStep = Step.AggregateStep()
        StepBuilder(aggregateStep.aggregate::add).apply(configBlock)
        addStep(aggregateStep)
    }

    fun `do`(configBlock: ConfigBlock<StepBuilder>) {
        val doStep = Step.DoStep()
        StepBuilder(doStep.`do`::add).apply(configBlock)
        addStep(doStep)
    }

    fun `try`(configBlock: ConfigBlock<StepBuilder>) =
            oneTimeStepBuilder("try") { addStep(Step.TryStep(it)) }.apply(configBlock)

    private fun oneTimeStepBuilder(configName: String, addStep: (Step) -> Any?): StepBuilder {
        var called = false
        return StepBuilder {
            if (called) throw IllegalStateException("$configName may only contain at most one step")
            called = true
            addStep(it)
        }
    }
}

fun StepBuilder.get(resource: String, configBlock: ConfigBlock<Step.GetStep<Object>>) =
        baseGet(resource, mutableMapOf(), configBlock)

fun StepBuilder.put(resource: String, configBlock: ConfigBlock<Step.PutStep<Object, Object>>) =
        basePut(resource, mutableMapOf(), mutableMapOf(), configBlock)
