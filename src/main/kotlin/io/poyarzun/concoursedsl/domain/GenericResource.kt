package io.poyarzun.concoursedsl.domain

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslMap

@JsonDeserialize(using = JsonDeserializer.None::class)
class GenericResource(name: String, type: String) : Resource<Source>(name, type) {
    override val source: Source = DslMap.empty()
}

fun resource(name: String, type: String, configBlock: ConfigBlock<Resource<Source>>) =
        GenericResource(name, type).apply(configBlock)
