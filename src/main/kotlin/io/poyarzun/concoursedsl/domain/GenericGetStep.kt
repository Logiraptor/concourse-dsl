package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslMap

class GenericGetStep(name: String) : Step.GetStep<Params>(name) {
    override val params: Params = DslMap.empty()
}

fun get(resource: String, configBlock: ConfigBlock<GenericGetStep>) =
        GenericGetStep(resource).apply(configBlock)