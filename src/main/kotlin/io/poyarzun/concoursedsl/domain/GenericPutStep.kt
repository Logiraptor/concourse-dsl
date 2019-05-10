package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslMap

class GenericPutStep(name: String) : Step.PutStep<Params, Params>(name) {
    override val params: Params = DslMap.empty()
    override val getParams: Params = DslMap.empty()
}

fun put(resource: String, configBlock: ConfigBlock<GenericPutStep>) =
        GenericPutStep(resource).apply(configBlock)
