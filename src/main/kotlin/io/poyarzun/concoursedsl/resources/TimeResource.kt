package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslObject
import io.poyarzun.concoursedsl.dsl.baseGet
import io.poyarzun.concoursedsl.dsl.basePut

class TimeResource(name: String) : Resource<DslObject<TimeResource.SourceParams>>(name, "time") {
    override val source = DslObject.from(::SourceParams)

    class SourceParams {
        var interval: String? = null
        var location: String? = null
        var start: String? = null
        var stop: String? = null
        var days: String? = null
    }

    class GetParams

    class PutParams
}

fun timeResource(name: String, configBlock: ConfigBlock<TimeResource>) =
        TimeResource(name).apply(configBlock)

fun get(repo: TimeResource) =
        baseGet(repo.name, TimeResource.GetParams()) {}

fun put(repo: TimeResource) =
        basePut(repo.name, TimeResource.PutParams(), TimeResource.GetParams()) {}
