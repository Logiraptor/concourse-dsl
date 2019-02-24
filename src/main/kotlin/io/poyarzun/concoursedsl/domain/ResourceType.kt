package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.Params
import io.poyarzun.concoursedsl.dsl.Source
import io.poyarzun.concoursedsl.dsl.Tags

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ResourceType(
        val name: String,
        val type: String,
        val source: Source = mutableMapOf(),
        val tags: Tags = mutableListOf(),
        val params: Params = mutableMapOf(),
        var privileged: Boolean? = null,
        var checkEvery: String = ""
)