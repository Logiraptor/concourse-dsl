package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.poyarzun.concoursedsl.dsl.Init

sealed class Step : StepHookReceiver {
    var tags: MutableList<String>? = null
    var timeout: String? = null
    var attempts: Int? = null

    @JsonProperty("on_success")
    override var onSuccess: Step? = null
    @JsonProperty("on_failure")
    override var onFailure: Step? = null
    @JsonProperty("on_abort")
    override var onAbort: Step? = null

    override var ensure: Step? = null

    data class GetStep<InProps>(
        val get: String,
        var params: InProps,
        var resource: String? = null,
        var version: String? = null,
        var passed: List<String>? = null,
        var trigger: Boolean? = null
    ) : Step() {
        operator fun InProps.invoke(init: Init<InProps>) {
            this.apply(init)
        }
    }

    data class PutStep<InProps, OutProps>(
        val put: String,
        val params: OutProps,
        var resource: String? = null,
        @JsonProperty("get_params")
        var getParams: InProps? = null
    ) : Step() {
        operator fun OutProps.invoke(init: Init<OutProps>) {
            this.apply(init)
        }
    }

    data class TaskStep(
        val task: String,

        // TODO: At most one of these is required
        var config: Task? = null,
        var file: String? = null,

        var privileged: Boolean? = null,
        var params: Map<String, Any?>? = null,
        var image: String? = null,
        @JsonProperty("input_mapping")
        var inputMapping: Map<String, String>? = null,
        @JsonProperty("output_mapping")
        var outputMapping: Map<String, String?>? = null
    ) : Step()

    data class AggregateStep(val aggregate: MutableList<Step> = ArrayList()) : Step()

    data class DoStep(val `do`: MutableList<Step> = ArrayList()) : Step()

    data class TryStep(val `try`: Step) : Step()
}
