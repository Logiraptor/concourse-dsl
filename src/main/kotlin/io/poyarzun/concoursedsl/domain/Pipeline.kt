package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Pipeline(
    var jobs: MutableList<Job> = ArrayList(),
    var groups: MutableList<Group> = ArrayList(),
    var resources: MutableList<Resource<Any>> = ArrayList(),
    @JsonProperty("resource_types")
    var resourceTypes: MutableList<ResourceType> = ArrayList()
)

