package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.DslList

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class Pipeline {
    var jobs = DslList.empty<Job>()
    var groups = DslList.empty<Group>()
    var resources = DslList.empty<Resource<*>>()
    var resourceTypes = DslList.empty<ResourceType>()
}

