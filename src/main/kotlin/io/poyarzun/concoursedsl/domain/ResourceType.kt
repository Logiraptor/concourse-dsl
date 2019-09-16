package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslMap

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ResourceType(val name: String, val type: String) {
    val source: Source = DslMap.empty()
    val tags: Tags = DslList.empty()
    val params: Params = DslMap.empty()
    var privileged: Boolean? = null
    var checkEvery: String? = null
    var uniqueVersionHistory: Boolean? = null
}

fun resourceType(name: String, type: String, configBlock: ConfigBlock<ResourceType>) =
        ResourceType(name, type).apply(configBlock)
