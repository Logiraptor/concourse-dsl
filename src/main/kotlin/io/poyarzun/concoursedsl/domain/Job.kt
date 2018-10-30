package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Job(
        val name: String,
        val plan: MutableList<Step> = ArrayList(),
        var serial: Boolean? = null,
        @JsonProperty("build_logs_to_retain")
        var buildLogsToRetain: Int? = null,
        @JsonProperty("serial_groups")
        var serialGroups: MutableList<String>? = null,
        @JsonProperty("max_in_flight")
        var maxInFlight: Int? = null,
        var public: Boolean? = null,
        @JsonProperty("disable_manual_trigger")
        var disableManualTrigger: Boolean? = null,
        var interruptible: Boolean? = null,

        @JsonProperty("on_success")
        override var onSuccess: Step? = null,
        @JsonProperty("on_failure")
        override var onFailure: Step? = null,
        @JsonProperty("on_abort")
        override var onAbort: Step? = null,
        override var ensure: Step? = null
) : StepHookReceiver