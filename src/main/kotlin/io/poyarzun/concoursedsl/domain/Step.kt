package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.Tags

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
sealed class Step : StepHookReceiver {
    val tags: Tags = DslList.empty()
    var timeout: String? = null
    var attempts: Int? = null

    override var onSuccess: Step? = null
    override var onFailure: Step? = null
    override var onAbort: Step? = null

    override var ensure: Step? = null

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class GetStep<Params: Any>(
        val get: String,
        val params: Params,
        var resource: String? = null,
        var version: String? = null,
        var passed: MutableList<String>? = null,
        var trigger: Boolean? = null
    ) : Step()

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class PutStep<GetParams: Any, PutParams: Any>(
        val put: String,
        val params: PutParams,
        val getParams: GetParams,
        var resource: String? = null
    ) : Step()

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class TaskStep(
        val task: String,
        val inputMapping: MutableMap<String, String> = mutableMapOf(),
        val outputMapping: MutableMap<String, String> = mutableMapOf(),

        // TODO: At most one of these is required
        var config: Task? = null,
        var file: String? = null,

        var privileged: Boolean? = null,
        var params: Map<String, Any?>? = null,
        var image: String? = null
    ) : Step()

    data class AggregateStep(val aggregate: MutableList<Step> = ArrayList()) : Step()

    data class DoStep(val `do`: MutableList<Step> = ArrayList()) : Step()

    data class TryStep(val `try`: Step) : Step()
}
