package io.poyarzun.concoursedsl.domain

data class Config(
    var resources: MutableList<Resource> = ArrayList(),
    var jobs: MutableList<Job> = ArrayList()
)

data class Resource(
    val name: String,
    val type: String,
    var source: Map<String, Any> = emptyMap()
)

data class Job(val name: String, var plan: MutableList<Step> = ArrayList())

sealed class Step() {
    abstract var passed: List<String>?

    data class GetStep(
        val get: String,
        var trigger: Boolean = false,
        override var passed: List<String>? = null
    ) : Step()

    data class PutStep(
        val put: String,
        override var passed: List<String>? = null
    ) : Step()

    data class TaskStep(
        val task: String,
        var file: String? = null,
        override var passed: List<String>? = null
    ) : Step()
}

