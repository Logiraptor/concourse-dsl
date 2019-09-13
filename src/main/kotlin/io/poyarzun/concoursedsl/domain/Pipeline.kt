package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslList

@ConcourseDslMarker
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class Pipeline {
    var jobs = DslList.empty<Job>()
    var groups = DslList.empty<Group>()
    var resources = DslList.empty<Resource<*>>()
    var resourceTypes = DslList.empty<ResourceType>()
}

/**
 * Shorthand for declaring a pipeline and configuring it with [configBlock]
 */
fun pipeline(configBlock: ConfigBlock<Pipeline>) =
        Pipeline().apply(configBlock)
