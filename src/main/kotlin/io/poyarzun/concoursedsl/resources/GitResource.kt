package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslObject

class GitResource(name: String) : Resource<DslObject<GitResource.SourceParams>>(name, "git") {
    override val source = DslObject.from(::SourceParams)

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class SourceParams(val uri: String) {
        var branch: String? = null
        var privateKey: String? = null
        var username: String? = null
        var password: String? = null
        var paths = DslList.empty<String>()
        var ignorePaths = DslList.empty<String>()
        var skipSslVerification: Boolean? = null
        var tagFilter: String? = null
        var gitConfig = DslList.empty<Config>()
        var disableCiSkip: Boolean? = null
        var commitVerificationKeys = DslList.empty<String>()
        var commitVerificationKeyIds = DslList.empty<String>()
        var gpgKeyserver: String? = null
        var gitCryptKey: String? = null
        val httpsTunnel = DslObject.from(::HttpProxy)
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Config(val name: String, val value: String)

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class HttpProxy(val proxyHost: String, val proxyPort: String) {
        var proxyUser: String? = null
        var proxyPassword: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class GetParams {
        var depth: Int? = null
        // TODO: List<String> or 'all'
        var submodules: Any? = null
        var submoduleRecursive: Boolean? = null
        var submoduleRemote: Boolean? = null
        var disableGitLfs: Boolean? = null
        var cleanTags: Boolean? = null
        var shortRefFormat: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class PutParams(val repository: String) {
        var rebase: Boolean? = null
        var merge: Boolean? = null
        var tag: String? = null
        var onlyTag: Boolean? = null
        var tagPrefix: String? = null
        var force: Boolean? = null
        var annotate: String? = null
        var notes: String? = null
    }

    class GetStep(name: String) : Step.GetStep<DslObject<GetParams>>(name) {
        override val params = DslObject.from(::GetParams)
    }

    class PutStep(name: String) : Step.PutStep<DslObject<GetParams>, DslObject<PutParams>>(name) {
        override val params = DslObject.from(::PutParams)
        override val getParams = DslObject.from(::GetParams)
    }
}

fun gitResource(name: String, configBlock: ConfigBlock<GitResource>) =
        GitResource(name).apply(configBlock)

fun get(repo: GitResource, configBlock: ConfigBlock<GitResource.GetStep>) =
        GitResource.GetStep(repo.name).apply(configBlock)

fun put(repo: GitResource, configBlock: ConfigBlock<GitResource.PutStep>) =
        GitResource.PutStep(repo.name).apply(configBlock)
