package io.poyarzun.concoursedsl.resources

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslObject
import io.poyarzun.concoursedsl.dsl.baseGet
import io.poyarzun.concoursedsl.dsl.basePut

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
}

fun cfResource(name: String, configBlock: ConfigBlock<CfResource>) =
        CfResource(name).apply(configBlock)

fun get(resource: CfResource, configBlock: ConfigBlock<Step.GetStep<CfResource.GetParams>>) =
        baseGet(resource.name, CfResource.GetParams(), configBlock)

fun put(resource: CfResource, manifest: String, configBlock: ConfigBlock<Step.PutStep<CfResource.GetParams, CfResource.PutParams>>) =
        basePut(resource.name, CfResource.PutParams(manifest), CfResource.GetParams(), configBlock)