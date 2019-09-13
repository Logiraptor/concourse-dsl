package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.ConcourseDslMarker
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslObject

// https://github.com/concourse/semver-resource

enum class Bump {
    major, minor, patch, final
}

class SemverResource(name: String) : Resource<SemverResource.SourceParams>(name, "semver") {
    override val source = SourceParams()

    object Git
    object Gcs
    object S3
    object Swift

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class SourceParams : DslObject<SemverDriver>(null) {
        operator fun invoke(driver: Git, uri: String, branch: String, file: String, configBlock: ConfigBlock<GitDriver>) {
            value = GitDriver(uri, branch, file).apply(configBlock)
        }

        operator fun invoke(driver: Gcs, bucket: String, key: String, jsonKey: String, configBlock: ConfigBlock<GcsDriver>) {
            value = GcsDriver(bucket, key, jsonKey).apply(configBlock)
        }

        operator fun invoke(driver: S3, bucket: String, key: String, accessKeyId: String, secretAccessKey: String, configBlock: ConfigBlock<S3Driver>) {
            value = S3Driver(bucket, key, accessKeyId, secretAccessKey).apply(configBlock)
        }

        operator fun invoke(driver: Swift, configBlock: ConfigBlock<SwiftDriver>) {
            value = SwiftDriver().apply(configBlock)
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class GetParams {
        var bump: Bump? = null
        var pre: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class PutParams {
        var file: String? = null
        var bump: Bump? = null
        var pre: String? = null
    }

    class GetStep(name: String) : Step.GetStep<DslObject<GetParams>>(name) {
        override val params = DslObject.from(SemverResource::GetParams)
    }

    class PutStep(name: String) : Step.PutStep<DslObject<GetParams>, DslObject<PutParams>>(name) {
        override val params = DslObject.from(SemverResource::PutParams)
        override val getParams = DslObject.from(SemverResource::GetParams)
    }

    @ConcourseDslMarker
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    open class SemverDriver {
        var initialVersion: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class GitDriver(
            val uri: String,
            val branch: String,
            val file: String) : SemverDriver() {
        val driver = "git"
        var privateKey: String? = null
        var username: String? = null
        var password: String? = null
        var gitUser: String? = null
        var depth: Int? = null
        var commitMessage: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class S3Driver(
            val bucket: String,
            val key: String,
            val accessKeyId: String,
            val secretAccessKey: String) : SemverDriver() {
        val driver = "s3"
        var regionName: String? = null
        var endpoint: String? = null
        var disableSsl: Boolean? = null
        var skipSslVerification: Boolean? = null
        var serverSideEncryption: String? = null
        var useV2Signing: Boolean? = null
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class GcsDriver(val bucket: String, val key: String, val jsonKey: String) : SemverDriver() {
        val driver = "gcs"
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    class SwiftDriver : SemverDriver() {
        val driver = "swift"
        val openstack = DslObject.from(::OpenStack)
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class OpenStack(val container: String, val itemName: String, val region: String) {
        var identityEndpoint: String? = null
        var username: String? = null
        var userId: String? = null
        var password: String? = null
        var apiKey: String? = null
        var domainId: String? = null
        var domainName: String? = null
        var tenantId: String? = null
        var tenantName: String? = null
        var allowReauth: Boolean? = null
        var tokenId: String? = null
    }
}

fun semverResource(name: String, configBlock: ConfigBlock<SemverResource>) =
        SemverResource(name).apply(configBlock)

fun get(resource: SemverResource, configBlock: ConfigBlock<SemverResource.GetStep>) =
        SemverResource.GetStep(resource.name).apply(configBlock)

fun put(resource: SemverResource, configBlock: ConfigBlock<SemverResource.PutStep>) =
        SemverResource.PutStep(resource.name).apply(configBlock)
