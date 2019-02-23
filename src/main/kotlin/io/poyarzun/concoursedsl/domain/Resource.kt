package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Resource<SourceParams>(
    val name: String,
    val type: String,
    val source: SourceParams,
    var version: Map<String, Any?>? = null,
    @JsonProperty("check_every")
    var checkEvery: String? = null,
    var tags: MutableList<String>? = null,
    @JsonProperty("webhook_token")
    var webhookToken: String? = null
)
