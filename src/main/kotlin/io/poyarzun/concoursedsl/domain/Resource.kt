package io.poyarzun.concoursedsl.domain

data class Resource(
    val name: String,
    val type: String,
    var source: Map<String, Any?>? = null,
    var version: Map<String, Any?>? = null,
    var checkEvery: String? = null,
    var tags: MutableList<String>? = null,
    var webhookToken: String? = null
)