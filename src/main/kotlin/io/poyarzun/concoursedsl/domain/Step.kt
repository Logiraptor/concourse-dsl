package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.*

@ConcourseDslMarker
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = StepDeserializer::class)
sealed class Step {
    val tags: Tags = DslList.empty()
    var timeout: String? = null
    var attempts: Int? = null

    var onSuccess: Step? = null
    var onFailure: Step? = null
    var onAbort: Step? = null
    var ensure: Step? = null

    @NoArg
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonDeserialize(using = JsonDeserializer.None::class)
    abstract class GetStep<Params : Any>(val get: String) : Step() {
        abstract val params: Params
        var resource: String? = null
        var version: String? = null
        var passed = DslList.empty<String>()
        var trigger: Boolean? = null
    }

    @NoArg
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonDeserialize(using = JsonDeserializer.None::class)
    abstract class PutStep<GetParams : Any, PutParams : Any>(val put: String) : Step() {
        abstract val params: PutParams
        abstract val getParams: GetParams
        var resource: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonDeserialize(using = JsonDeserializer.None::class)
    data class TaskStep(val task: String) : Step() {
        val inputMapping = DslMap.empty<String, String>()
        val outputMapping = DslMap.empty<String, String>()

        // TODO: At most one of these is required
        @JsonDeserialize(using = TaskDslObjectDeserializer::class)
        var config = DslObject.from(::Task)
        var file: String? = null

        var privileged: Boolean? = null
        var params: Params = DslMap.empty()
        var image: String? = null
    }

    @JsonDeserialize(using = JsonDeserializer.None::class)
    class AggregateStep : Step() {
        val aggregate: MutableList<Step> = mutableListOf()

        operator fun Step.unaryPlus() = this@AggregateStep.aggregate.add(this)
    }

    @JsonDeserialize(using = JsonDeserializer.None::class)
    class DoStep : Step() {
        val `do`: MutableList<Step> = mutableListOf()

        operator fun Step.unaryPlus() = this@DoStep.`do`.add(this)
    }

    @JsonDeserialize(using = JsonDeserializer.None::class)
    class TryStep : Step() {
        lateinit var `try`: Step
    }
}

fun `try`(init: Step.TryStep.() -> Step) =
        Step.TryStep().apply { `try` = init() }

fun `do`(configBlock: ConfigBlock<Step.DoStep>): Step.DoStep =
        Step.DoStep().apply(configBlock)

fun aggregate(configBlock: ConfigBlock<Step.AggregateStep>) =
        Step.AggregateStep().apply(configBlock)

