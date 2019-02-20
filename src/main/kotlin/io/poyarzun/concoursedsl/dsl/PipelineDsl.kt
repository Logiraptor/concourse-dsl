package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*

/**
 * Shorthand for declaring a pipeline and configuring it with [init]
 */
fun pipeline(init: Init<Pipeline>) =
    Pipeline().apply(init)

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
fun Pipeline.resource(name: String, type: String, init: Init<Resource<Object>>) =
    baseResource(name, type, mutableMapOf(), init)

/**
 * Declare a resource named [name] of type [type] in the pipeline and configure it with [init]
 *
 * @see Resource
 */
fun <SourceProps> Pipeline.baseResource(name: String, type: String, sourceProps: SourceProps, init: Init<Resource<SourceProps>>) =
        (Resource(name, type, sourceProps).apply(init)).also { resources.add(it as Resource<Any>) }

/**
 * Declare a resource type named [name] of type [type] in the pipeline and configure it with [init]
 *
 * @see ResourceType
 */
fun Pipeline.resourceType(name: String, type: String, init: Init<ResourceType>) =
    resourceTypes.add(ResourceType(name, type).apply(init))
