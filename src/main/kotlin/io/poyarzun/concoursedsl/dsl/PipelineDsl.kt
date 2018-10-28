package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*

/**
 * Declare a job named [name] in the pipeline and configure it with [init]
 *
 * @see Job
 */
fun Pipeline.job(name: String, init: Init<Job>) =
    jobs.add(Job(name).apply(init))

/**
 * Declare a group named [name] in the pipeline and configure it with [init]
 *
 * @see Group
 */
fun Pipeline.group(name: String, init: Init<Group>) =
    groups.add(Group(name).apply(init))

/**
 * Declare a resource named [name] of type [type] in the pipeline and configure it with [init]
 *
 * @see Resource
 */
fun Pipeline.resource(name: String, type: String, init: Init<Resource>) =
    resources.add(Resource(name, type).apply(init))

/**
 * Declare a resource type named [name] of type [type] in the pipeline and configure it with [init]
 *
 * @see ResourceType
 */
fun Pipeline.resourceType(name: String, type: String, init: Init<ResourceType>) =
    resourceTypes.add(ResourceType(name, type).apply(init))