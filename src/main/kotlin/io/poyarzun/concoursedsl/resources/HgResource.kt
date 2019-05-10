package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslObject

class HgResource(name: String) : Resource<DslObject<HgResource.SourceParams>>(name, "hg") {
    override val source = DslObject.from(::SourceParams)

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class SourceParams(val uri: String) {
        var branch: String? = null
        var privateKey: String? = null
        val paths = DslList.empty<String>()
        val ignorePaths = DslList.empty<String>()
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

    class GetStep(name: String) : Step.GetStep<DslObject<GetParams>>(name) {
        override val params = DslObject.from(::GetParams)
    }

    class PutStep(name: String) : Step.PutStep<DslObject<GetParams>, DslObject<PutParams>>(name) {
        override val params = DslObject.from(::PutParams)
        override val getParams = DslObject.from(::GetParams)
    }
}

fun hgResource(name: String, configBlock: ConfigBlock<HgResource>) =
        HgResource(name).apply(configBlock)

fun get(resource: HgResource, configBlock: ConfigBlock<HgResource.GetStep>) =
        HgResource.GetStep(resource.name).apply(configBlock)

fun put(repo: HgResource, configBlock: ConfigBlock<HgResource.PutStep>) =
        HgResource.PutStep(repo.name).apply(configBlock)
