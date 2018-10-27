package io.poyarzun.concoursedsl.domain

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
    var onSuccess: Step? = null,
    var onFailure: Step? = null,
    var onAbort: Step? = null,
    var ensure: Step? = null
)