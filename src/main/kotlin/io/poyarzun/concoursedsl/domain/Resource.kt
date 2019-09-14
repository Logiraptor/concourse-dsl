package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeId
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslMap
import io.poyarzun.concoursedsl.dsl.ResourceDeserializer
import io.poyarzun.concoursedsl.dsl.StepDeserializer

@ConcourseDslMarker
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = ResourceDeserializer::class)
abstract class Resource<Source : Any>(val name: String, val type: String) {
    abstract val source: Source

    val tags: Tags = DslList.empty()
    val version: Version = DslMap.empty()
    var checkEvery: String? = null
    var webhookToken: String? = null
}
