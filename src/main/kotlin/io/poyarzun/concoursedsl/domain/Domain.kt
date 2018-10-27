package io.poyarzun.concoursedsl.domain

data class Pipeline(
    var jobs: MutableList<Job> = ArrayList(),
    var groups: MutableList<Group> = ArrayList(),
    var resources: MutableList<Resource> = ArrayList(),
    var resourceTypes: MutableList<ResourceType> = ArrayList()
)

data class Resource(
    val name: String,
    val type: String,
    var source: Map<String, Any?>? = null,
    var version: Map<String, Any?>? = null,
    var checkEvery: String? = null,
    var tags: MutableList<String>? = null,
    var webhookToken: String? = null
)

data class Job(
    val name: String,
    var plan: MutableList<Step> = ArrayList(),
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

data class ResourceType(
    val name: String,
    val type: String,
    var source: Map<String, Any?>? = null,
    var privileged: Boolean? = null,
    var params: Map<String, Any?>? = null,
    var checkEvery: String? = null,
    var tags: MutableList<String>? = null
)

data class Group(
    val name: String,
    val jobs: MutableList<String> = ArrayList(),
    val resources: MutableList<String> = ArrayList()
)

sealed class Step {

    data class GetStep(
        val get: String,
        var resource: String? = null,
        var version: String? = null,
        var passed: List<String>? = null,
        var params: Map<String, Any?>? = null,
        var trigger: Boolean? = null
    ) : Step()

    data class PutStep(
        val put: String,
        var resource: String? = null,
        var params: Map<String, Any?>? = null,
        var getParams: Map<String, Any?>? = null
    ) : Step()

    data class TaskStep(
        val task: String,
        var file: String? = null,
        var passed: List<String>? = null
    ) : Step()

    data class AggregateStep(val aggregate: MutableList<Step> = ArrayList()): Step()

    data class DoStep(val steps: MutableList<Step> = ArrayList()): Step()

    data class TryStep(val step: Step): Step()
}

