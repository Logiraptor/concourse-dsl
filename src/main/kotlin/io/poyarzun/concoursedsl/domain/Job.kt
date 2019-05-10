package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslList

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Job(val name: String) {
    val plan = DslList.empty<Step>()
    var serial: Boolean? = null
    var buildLogsToRetain: Int? = null
    val serialGroups = DslList.empty<String>()
    var maxInFlight: Int? = null
    var public: Boolean? = null
    var disableManualTrigger: Boolean? = null
    var interruptible: Boolean? = null

    var onSuccess: Step? = null
    var onFailure: Step? = null
    var onAbort: Step? = null
    var ensure: Step? = null
}

fun job(name: String, configBlock: ConfigBlock<Job>) =
        Job(name).apply(configBlock)
