package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.poyarzun.concoursedsl.dsl.Init

data class Resource<SourceProps>(
    val name: String,
    val type: String,
    val source: SourceProps,
    var version: Map<String, Any?>? = null,
    @JsonProperty("check_every")
    var checkEvery: String? = null,
    var tags: MutableList<String>? = null,
    @JsonProperty("webhook_token")
    var webhookToken: String? = null
) {
    operator fun SourceProps.invoke(init: Init<SourceProps>) {
        this.apply(init)
    }
}
