package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslMap

@NoArg
class GenericGetStep(get: String) : Step.GetStep<Params>(get) {
    override val params: Params = DslMap.empty()
}

fun get(resource: String, configBlock: ConfigBlock<GenericGetStep>) =
        GenericGetStep(resource).apply(configBlock)