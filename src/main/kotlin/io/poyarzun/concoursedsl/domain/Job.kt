package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Job(
        val name: String,
        val plan: MutableList<Step> = ArrayList(),
        var serial: Boolean? = null,
        var buildLogsToRetain: Int? = null,
        var serialGroups: MutableList<String>? = null,
        var maxInFlight: Int? = null,
        var public: Boolean? = null,
        var disableManualTrigger: Boolean? = null,
        var interruptible: Boolean? = null,

        override var onSuccess: Step? = null,
        override var onFailure: Step? = null,
        override var onAbort: Step? = null,
        override var ensure: Step? = null
) : StepHookReceiver