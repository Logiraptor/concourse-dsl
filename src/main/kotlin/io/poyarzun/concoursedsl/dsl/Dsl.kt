package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.poyarzun.concoursedsl.domain.Config
import io.poyarzun.concoursedsl.domain.Job
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step

typealias Part<T> = T.() -> Unit

fun Part<Config>.generateYML(): String {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule())
    val config = Config()
    this.invoke(config)
    return mapper.writeValueAsString(config)
}

fun pipeline(init: Part<ConfigWrapper>): Part<Config> {
    return {
        ConfigWrapper(this).apply(init).config
    }
}


class ConfigWrapper(val config: Config) {
    fun job(name: String, init: Part<Job>) {
        val job = Job(name)
        job.init()
        config.jobs.add(job)
    }

    fun resource(name: String, type: String, init: Part<Resource>) {
        val resource = Resource(name, type)
        resource.init()
        config.resources.add(resource)
    }
}

fun Job.plan(init: Part<PlanWrapper>) {
    PlanWrapper(this).init()
}

class PlanWrapper(val job: Job) {
    fun get(resource: String, init: Part<Step.GetStep>): Step.GetStep {
        val step = Step.GetStep(resource)
        step.init()
        job.plan.add(step)
        return step
    }

    fun put(resource: String, init: Part<Step.PutStep>): Step.PutStep {
        val step = Step.PutStep(resource)
        step.init()
        job.plan.add(step)
        return step
    }

    fun task(name: String, init: Part<Step.TaskStep>): Step.TaskStep {
        val step = Step.TaskStep(name)
        step.init()
        job.plan.add(step)
        return step
    }
}

