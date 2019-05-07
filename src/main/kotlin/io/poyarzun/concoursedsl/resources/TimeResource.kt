package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.baseGet
import io.poyarzun.concoursedsl.dsl.basePut
import io.poyarzun.concoursedsl.dsl.baseResource

object Time {
    class SourceParams(
            var interval: String? = null,
            var location: String? = null,
            var start: String? = null,
            var stop: String? = null,
            var days: String? = null
    )

    class GetParams

    class PutParams
}

fun Pipeline.timeResource(name: String, configBlock: ConfigBlock<Resource<Time.SourceParams>>) =
        this.baseResource(name, "time", Time.SourceParams(), configBlock)

fun get(repo: Resource<Time.SourceParams>) =
        baseGet(repo.name, Time.GetParams()) {}

fun put(repo: Resource<Time.SourceParams>) =
        basePut(repo.name, Time.PutParams(), Time.GetParams()) {}
