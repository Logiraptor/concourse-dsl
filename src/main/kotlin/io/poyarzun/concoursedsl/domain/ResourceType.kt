package io.poyarzun.concoursedsl.domain

data class ResourceType(
    val name: String,
    val type: String,
    var source: Map<String, Any?>? = null,
    var privileged: Boolean? = null,
    var params: Map<String, Any?>? = null,
    var checkEvery: String? = null,
    var tags: MutableList<String>? = null
)