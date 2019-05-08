package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslObject

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

    class GetStep(name: String) : Step.GetStep<DslObject<GetParams>>(name) {
        override val params = DslObject.from(::GetParams)
    }

    class PutStep(name: String) : Step.PutStep<DslObject<GetParams>, DslObject<PutParams>>(name) {
        override val params = DslObject.from(::PutParams)
        override val getParams = DslObject.from(::GetParams)
    }
}

fun timeResource(name: String, configBlock: ConfigBlock<TimeResource>) =
        TimeResource(name).apply(configBlock)

fun get(repo: TimeResource) =
        TimeResource.GetStep(repo.name)

fun put(repo: TimeResource) =
        TimeResource.PutStep(repo.name)
