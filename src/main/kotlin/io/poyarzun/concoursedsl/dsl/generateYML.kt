package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.poyarzun.concoursedsl.domain.Pipeline

fun generateYML(pipeline: Pipeline): String {
    return objectMapper().writeValueAsString(pipeline)
}

fun readYML(pipelineSource: String): Pipeline {
    return objectMapper().readValue(pipelineSource)
}

private fun objectMapper(): ObjectMapper {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule())
    mapper.registerModule(DslModule)
    return mapper
}
