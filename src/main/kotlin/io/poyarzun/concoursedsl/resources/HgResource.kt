package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslObject
import io.poyarzun.concoursedsl.dsl.baseGet
import io.poyarzun.concoursedsl.dsl.basePut

class HgResource(name: String) : Resource<DslObject<HgResource.SourceParams>>(name, "hg") {
    override val source = DslObject.from(::SourceParams)

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SourceParams(val uri: String) {
        var branch: String? = null
        var privateKey: String? = null
        var paths: MutableList<String>? = null
        var ignorePaths: MutableList<String>? = null
        var skipSslVerification: Boolean? = null
        var tagFilter: String? = null
        var revsetFilter: String? = null
    }

    class GetParams

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class PutParams(val repository: String) {
        var rebase: Boolean? = null
        var tag: String? = null
        var tagPrefix: String? = null
    }
}

fun hgResource(name: String, configBlock: ConfigBlock<HgResource>) =
        HgResource(name).apply(configBlock)

fun get(resource: HgResource, configBlock: ConfigBlock<Step.GetStep<HgResource.GetParams>>) =
        baseGet(resource.name, HgResource.GetParams(), configBlock)

fun put(resource: HgResource, repository: String, configBlock: ConfigBlock<Step.PutStep<HgResource.GetParams, HgResource.PutParams>>) =
        basePut(resource.name, HgResource.PutParams(repository), HgResource.GetParams(), configBlock)
