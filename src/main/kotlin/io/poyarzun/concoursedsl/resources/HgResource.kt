package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.dsl.Init
import io.poyarzun.concoursedsl.dsl.baseResource

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class HgSourceProps(
        val uri: String,
        var branch: String? = null,
        var privateKey: String? = null,
        var paths: MutableList<String>? = null,
        var ignorePaths: MutableList<String>? = null,
        var skipSslVerification: Boolean? = null,
        var tagFilter: String? = null,
        var revsetFilter: String? = null
)

fun Pipeline.hgResource(name: String, uri: String, init: Init<Resource<HgSourceProps>>) =
        this.baseResource(name, "git", HgSourceProps(uri), init)
