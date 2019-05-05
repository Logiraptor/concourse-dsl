package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.domain.StepHookReceiver

typealias Source = DslMap<String, Any?>
typealias Params = DslMap<String, Any?>
typealias Version = DslMap<String, String>

typealias Tags = DslList<String>

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
    fun <InProps: Any> baseGet(resource: String, inProps: InProps, configBlock: ConfigBlock<Step.GetStep<InProps>>) =
            addStep(Step.GetStep(resource, inProps).apply(configBlock))

    fun <InProps: Any, OutProps: Any> basePut(resource: String, outProps: OutProps, inProps: InProps, configBlock: ConfigBlock<Step.PutStep<InProps, OutProps>>) =
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

fun StepBuilder.get(resource: String, configBlock: ConfigBlock<Step.GetStep<Params>>) =
        baseGet(resource, DslMap.empty(), configBlock)

fun StepBuilder.put(resource: String, configBlock: ConfigBlock<Step.PutStep<Params, Params>>) =
        basePut(resource, DslMap.empty(), DslMap.empty(), configBlock)
