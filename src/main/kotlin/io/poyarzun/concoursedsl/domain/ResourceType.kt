package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ResourceType(
        val name: String,
        val type: String,
        val source: Source = DslMap.empty(),
        val tags: Tags = DslList.empty(),
        val params: Params = DslMap.empty(),
        var privileged: Boolean? = null,
        var checkEvery: String = ""
)