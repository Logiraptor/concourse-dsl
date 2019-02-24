package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.Params
import io.poyarzun.concoursedsl.dsl.Source
import io.poyarzun.concoursedsl.dsl.Version

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Task(
    val platform: String,
    val run: RunConfig,
    var imageResource: Resource? = null,
    var rootfsUri: String? = null,
    val inputs: MutableList<Input> = mutableListOf(),
    val outputs: MutableList<Output> = mutableListOf(),
    val caches: MutableList<Cache> = mutableListOf(),
    val params: Params = mutableMapOf()
) {
    data class RunConfig(
        val path: String,
        var args: MutableList<String>? = null,
        var dir: String? = null,
        var user: String? = null
    )

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class Resource(
        val type: String,
        val source: Source = mutableMapOf(),
        var params: Params = mutableMapOf(),
        var version: Version = mutableMapOf()
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