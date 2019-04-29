package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.StepBuilder
import io.poyarzun.concoursedsl.dsl.baseResource

// https://github.com/concourse/semver-resource

interface SemverDriver

enum class Bump {
    major, minor, patch, final
}

object Semver {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SourceParams(
            var driver: SemverDriver,
            var initialVersion: String? = null
    )

    {

    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class GetParams(
            var bump: Bump? = null,
            var pre: String? = null
    )

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class PutParams(
            var file: String? = null,
            var bump: Bump? = null,
            var pre: String? = null
    )

}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class GitSemverDriver(
        val uri: String,
        val branch: String,
        val file: String,
        var privateKey: String? = null,
        var userName: String? = null,
        var password: String? = null,
        var gitUser: String? = null,
        var depth: Int? = null,
        var commitMessage: String? = null
) : SemverDriver

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class S3Driver(val bucket: String,
                    val key: String,
                    val accessKey: String,
                    val secretAccessKey: String,
                    var regionName: String? = null,
                    var endpoint: String? = null,
                    var disableSsl: Boolean? = null,
                    var skipSslVerification: Boolean? = null,
                    var serverSideEncryption: String? = null,
                    var useV2Signing: Boolean? = null) : SemverDriver

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class OpenStack(val container: String, val itemName: String, val region: String, val identityEndpoint: String, val username: String, val userId: String, val password: String, val apiKey: String, val domainId: String, val domainName: String, val tenantId: String, val tenantName: String, val allowReauth: Boolean, val tokenId: String)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SwiftDriver(val openStack: OpenStack) : SemverDriver



fun Pipeline.semverResource(name: String, driver: SemverDriver, configBlock: ConfigBlock<Resource<Semver.SourceParams>>) =
        this.baseResource(name, "semver", Semver.SourceParams(driver), configBlock)

fun StepBuilder.get(resource: Resource<Semver.SourceParams>, configBlock: ConfigBlock<Step.GetStep<Semver.GetParams>>) =
        this.baseGet(resource.name, Semver.GetParams(), configBlock)

fun StepBuilder.put(resource: Resource<Semver.SourceParams>, configBlock: ConfigBlock<Step.PutStep<Semver.GetParams, Semver.PutParams>>) =
        this.basePut(resource.name, Semver.PutParams(), Semver.GetParams(), configBlock)
