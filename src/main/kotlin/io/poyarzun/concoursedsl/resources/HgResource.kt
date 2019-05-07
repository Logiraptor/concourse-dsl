package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.baseGet
import io.poyarzun.concoursedsl.dsl.basePut
import io.poyarzun.concoursedsl.dsl.baseResource

object Hg {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SourceParams(
            val uri: String,
            var branch: String? = null,
            var privateKey: String? = null,
            var paths: MutableList<String>? = null,
            var ignorePaths: MutableList<String>? = null,
            var skipSslVerification: Boolean? = null,
            var tagFilter: String? = null,
            var revsetFilter: String? = null
    )

    class GetParams

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class PutParams (
            val repository: String,
            var rebase: Boolean? = null,
            var tag: String? = null,
            var tagPrefix: String? = null
    )
}

fun Pipeline.hgResource(name: String, uri: String, configBlock: ConfigBlock<Resource<Hg.SourceParams>>) =
        this.baseResource(name, "git", Hg.SourceParams(uri), configBlock)

fun get(resource: Resource<Hg.SourceParams>, configBlock: ConfigBlock<Step.GetStep<Hg.GetParams>>) =
        baseGet(resource.name, Hg.GetParams(), configBlock)

fun put(resource: Resource<Hg.SourceParams>, repository: String, configBlock: ConfigBlock<Step.PutStep<Hg.GetParams, Hg.PutParams>>) =
        basePut(resource.name, Hg.PutParams(repository), Hg.GetParams(), configBlock)
