package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslObject

// https://github.com/concourse/cf-resource
class CfResource(name: String) : Resource<DslObject<CfResource.SourceParams>>(name, "cf") {
    override val source = DslObject.from(::SourceParams)

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SourceParams(val api: String, val organization: String, val space: String) {
        var username: String? = null
        var password: String? = null
        var clientId: String? = null
        var clientSecret: String? = null
        var skipCertCheck: Boolean? = null
        var verbose: Boolean? = null
    }

    class GetParams

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class PutParams(val manifest: String) {
        var path: String? = null
        var currentAppName: String? = null
        var environmentVariables: Map<String, String>? = null
        var vars: Map<String, String>? = null
        var varsFiles: List<String>? = null
        var dockerUserName: String? = null
        var dockerPassword: String? = null
        var showAppLog: Boolean? = null
        var noStart: Boolean? = null
    }

    class GetStep(name: String) : Step.GetStep<DslObject<GetParams>>(name) {
        override val params = DslObject.from(::GetParams)
    }

    class PutStep(name: String) : Step.PutStep<DslObject<GetParams>, DslObject<PutParams>>(name) {
        override val params = DslObject.from(::PutParams)
        override val getParams = DslObject.from(::GetParams)
    }
}

fun cfResource(name: String, configBlock: ConfigBlock<CfResource>) =
        CfResource(name).apply(configBlock)

fun get(resource: CfResource, configBlock: ConfigBlock<CfResource.GetStep>) =
        CfResource.GetStep(resource.name).apply(configBlock)

fun put(resource: CfResource, configBlock: ConfigBlock<CfResource.PutStep>) =
        CfResource.PutStep(resource.name).apply(configBlock)