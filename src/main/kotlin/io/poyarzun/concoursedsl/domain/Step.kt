package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
sealed class Step : StepHookReceiver {
    val tags: Tags = DslList.empty()
    var timeout: String? = null
    var attempts: Int? = null

    // TODO: DslObject for hooks
    override var onSuccess: Step? = null
    override var onFailure: Step? = null
    override var onAbort: Step? = null
    override var ensure: Step? = null

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class GetStep<Params : Any>(
            val get: String,
            // TODO: DslObject for params
            val params: Params) : Step() {
        var resource: String? = null
        var version: String? = null
        var passed = DslList.empty<String>()
        var trigger: Boolean? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class PutStep<GetParams : Any, PutParams : Any>(
            val put: String,
            // TODO: DslObject for PutParams
            val params: PutParams,
            // TODO: DslObject for GetParams
            val getParams: GetParams) : Step() {
        var resource: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class TaskStep(val task: String) : Step() {
        val inputMapping = DslMap.empty<String, String>()
        val outputMapping = DslMap.empty<String, String>()

        // TODO: At most one of these is required
        var config = DslObject.from(::Task)
        var file: String? = null

        var privileged: Boolean? = null
        var params: Params = DslMap.empty()
        var image: String? = null
    }

    data class AggregateStep(val aggregate: DslList<Step> = DslList.empty()) : Step()

    data class DoStep(val `do`: DslList<Step> = DslList.empty()) : Step()

    // TODO: DslObject for Step
    data class TryStep(val `try`: Step) : Step()
}
