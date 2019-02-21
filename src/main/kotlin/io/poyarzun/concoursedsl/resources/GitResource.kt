package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.Init
import io.poyarzun.concoursedsl.dsl.StepBuilder
import io.poyarzun.concoursedsl.dsl.baseResource

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GitSourceProps(
        val uri: String,
        var branch: String? = null,
        var privateKey: String? = null,
        var username: String? = null,
        var password: String? = null,
        var paths: MutableList<String>? = null,
        var ignorePaths: MutableList<String>? = null,
        var skipSslVerification: Boolean? = null,
        var tagFilter: String? = null,
        var gitConfig: MutableList<GitConfig>? = null,
        var disableCiSkip: Boolean? = null,
        var commitVerificationKeys: MutableList<String>? = null,
        var commitVerificationKeyIds: MutableList<String>? = null,
        var gpgKeyserver: String? = null,
        var gitCryptKey: String? = null,
        var httpsTunnel: HttpProxy? = null
) {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class GitConfig(val name: String, val value: String)

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class HttpProxy(
            val proxyHost: String,
            val proxyPort: String,
            var proxyUser: String,
            var proxyPassword: String
    )
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GitInProps(
        var depth: Int? = null,
        // TODO: List<String> or 'all'
        var submodules: Any? = null,
        var submoduleRecursive: Boolean? = null,
        var submoduleRemote: Boolean? = null,
        var disableGitLfs: Boolean? = null,
        var cleanTags: Boolean? = null,
        var shortRefFormat: String? = null
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GitOutProps(
        val repository: String,
        var rebase: Boolean? = null,
        var merge: Boolean? = null,
        var tag: String? = null,
        var onlyTag: Boolean? = null,
        var tagPrefix: String? = null,
        var force: Boolean? = null,
        var annotate: String? = null,
        var notes: String? = null
)

fun Pipeline.gitResource(name: String, uri: String, init: Init<Resource<GitSourceProps>>) =
        this.baseResource(name, "git", GitSourceProps(uri), init)

fun StepBuilder.get(repo: Resource<GitSourceProps>, init: Init<Step.GetStep<GitInProps>>) =
        this.baseGet(repo.name, GitInProps(), init)

fun StepBuilder.put(repo: Resource<GitSourceProps>, repository: String, init: Init<Step.PutStep<GitInProps, GitOutProps>>) =
        this.basePut(repo.name, GitOutProps(repository), GitInProps(), init)
