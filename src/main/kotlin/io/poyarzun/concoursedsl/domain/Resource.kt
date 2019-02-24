package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.Tags
import io.poyarzun.concoursedsl.dsl.Version

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Resource<Source : Any>(
        val name: String,
        val type: String,
        val source: Source,
        val tags: Tags = mutableListOf(),
        val version: Version = mutableMapOf(),
        var checkEvery: String = "",
        var webhookToken: String = ""
)
