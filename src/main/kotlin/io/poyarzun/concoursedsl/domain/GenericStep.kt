package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslMap
import io.poyarzun.concoursedsl.dsl.Params

class GenericGetStep(name: String) : Step.GetStep<Params>(name) {
    override val params: Params = DslMap.empty()
}

class GenericPutStep(name: String) : Step.PutStep<Params, Params>(name) {
    override val params: Params = DslMap.empty()
    override val getParams: Params = DslMap.empty()
}

fun get(resource: String, configBlock: ConfigBlock<GenericGetStep>) =
        GenericGetStep(resource).apply(configBlock)

fun put(resource: String, configBlock: ConfigBlock<GenericPutStep>) =
        GenericPutStep(resource).apply(configBlock)
