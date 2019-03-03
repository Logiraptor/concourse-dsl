package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Task(val platform: String) {

    @JsonDeserialize(using = RunConfigDslObjectDeserializer::class)
    val run = DslObject.from(::RunConfig)
    @JsonDeserialize(using = ResourceDslObjectDeserializer::class)
    val imageResource = DslObject.from(::Resource)
    var rootfsUri: String? = null
    val inputs = DslList.empty<Input>()
    val outputs = DslList.empty<Output>()
    val caches = DslList.empty<Cache>()
    val params: Params = DslMap.empty()

    class RunConfig(val path: String) {
        val args = DslList.empty<String>()
        var dir: String? = null
        var user: String? = null
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class Resource(val type: String) {
        val source: Source = DslMap.empty()
        val params: Params = DslMap.empty()
        val version: Version = DslMap.empty()
    }

    data class Input(val name: String) {
        var path: String? = null
        var optional: Boolean? = null
    }

    data class Output(val name: String) {
        var path: String? = null
    }

    data class Cache(val path: String)
}

fun task(name: String, configBlock: ConfigBlock<Step.TaskStep>) =
        Step.TaskStep(name).apply(configBlock)

fun input(name: String, configBlock: ConfigBlock<Task.Input>) =
        Task.Input(name).apply(configBlock)

fun output(name: String, configBlock: ConfigBlock<Task.Output>) =
        Task.Output(name).apply(configBlock)

fun cache(name: String) =
        Task.Cache(name)
