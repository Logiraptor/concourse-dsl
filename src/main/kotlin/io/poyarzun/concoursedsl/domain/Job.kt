package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.DslList

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Job(val name: String) : StepHookReceiver {
    // TODO: DslList for plan
    val plan: MutableList<Step> = ArrayList()
    var serial: Boolean? = null
    var buildLogsToRetain: Int? = null
    val serialGroups = DslList.empty<String>()
    var maxInFlight: Int? = null
    var public: Boolean? = null
    var disableManualTrigger: Boolean? = null
    var interruptible: Boolean? = null

    // TODO: DslObject for hooks
    override var onSuccess: Step? = null
    override var onFailure: Step? = null
    override var onAbort: Step? = null
    override var ensure: Step? = null
}