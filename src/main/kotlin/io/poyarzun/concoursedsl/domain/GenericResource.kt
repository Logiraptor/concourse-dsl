package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslMap
import io.poyarzun.concoursedsl.dsl.Source

class GenericResource(name: String, type: String) : Resource<Source>(name, type) {
    override val source: Source = DslMap.empty()
}

fun resource(name: String, type: String, configBlock: ConfigBlock<Resource<Source>>) =
        GenericResource(name, type).apply(configBlock)
