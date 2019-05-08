package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
sealed class Step {
    val tags: Tags = DslList.empty()
    var timeout: String? = null
    var attempts: Int? = null

    var onSuccess: Step? = null
    var onFailure: Step? = null
    var onAbort: Step? = null
    var ensure: Step? = null

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    abstract class GetStep<Params : Any>(val get: String) : Step() {
        abstract val params: Params
        var resource: String? = null
        var version: String? = null
        var passed = DslList.empty<String>()
        var trigger: Boolean? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    abstract class PutStep<GetParams : Any, PutParams : Any>(val put: String) : Step() {
        abstract val params: PutParams
        abstract val getParams: GetParams
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

    data class TryStep(val `try`: Step) : Step()
}
