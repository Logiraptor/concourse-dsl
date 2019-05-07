package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*

/**
 * Shorthand for declaring a pipeline and configuring it with [configBlock]
 */
fun pipeline(configBlock: ConfigBlock<Pipeline>) =
    Pipeline().apply(configBlock)

/**
 * Declare a job named [name] in the pipeline and configurePlainObject it with [configBlock]
 *
 * @see Job
 */
fun Pipeline.job(name: String, configBlock: ConfigBlock<Job>) =
    jobs.add(Job(name).apply(configBlock))

/**
 * Declare a group named [name] in the pipeline and configurePlainObject it with [configBlock]
 *
 * @see Group
 */
fun Pipeline.group(name: String, configBlock: ConfigBlock<Group>) =
    groups.add(Group(name).apply(configBlock))

/**
 * Declare a resource named [name] of type [type] in the pipeline and configurePlainObject it with [configBlock]
 *
 * @see Resource
 */
fun resource(name: String, type: String, configBlock: ConfigBlock<Resource<Source>>) =
        GenericResource(name, type).apply(configBlock)

/**
 * Declare a resource type named [name] of type [type] in the pipeline and configurePlainObject it with [configBlock]
 *
 * @see ResourceType
 */
fun Pipeline.resourceType(name: String, type: String, configBlock: ConfigBlock<ResourceType>) =
    resourceTypes.add(ResourceType(name, type).apply(configBlock))
