package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Task(
    val platform: String,
    val run: RunConfig,
    @JsonProperty("image_resource")
    var imageResource: Resource? = null,
    @JsonProperty("rootfs_uri")
    var rootfsUri: String? = null,
    var inputs: MutableList<Input>? = null,
    var outputs: MutableList<Output>? = null,
    var caches: MutableList<Cache>? = null,
    var params: Map<String, String>? = null
) {
    data class RunConfig(
        val path: String,
        var args: MutableList<String>? = null,
        var dir: String? = null,
        var user: String? = null
    )

    data class Resource(
        val type: String,
        val source: Map<String, Any?>,
        var params: Map<String, Any?>? = null,
        var version: Map<String, Any?>? = null
    )

    data class Input(
        val name: String,
        var path: String? = null,
        var optional: Boolean? = null
    )

    data class Output(
        val name: String,
        var path: String? = null
    )

    data class Cache(
        val path: String
    )
}