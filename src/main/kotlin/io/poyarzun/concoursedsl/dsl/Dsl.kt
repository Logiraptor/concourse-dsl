package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.poyarzun.concoursedsl.domain.Config
import io.poyarzun.concoursedsl.domain.Job
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step


typealias Part<T> = T.() -> T

fun Part<Config>.generateYML(): String {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.registerModule(KotlinModule())
    val example = this.invoke(Config())
    val yaml = mapper.writeValueAsString(example)
    return yaml
}

fun pipeline(init: ConfigWrapper.() -> Unit): Part<Config> {
    return {
        ConfigWrapper(this).apply(init).config
    }
}


class ConfigWrapper(val config: Config) {
    fun job(name: String, init: Job.() -> Unit) {
        val job = Job(name)
        job.init()
        config.jobs.add(job)
    }

    fun resource(name: String, type: String, init: Resource.() -> Unit) {
        val resource = Resource(name, type)
        resource.init()
        config.resources.add(resource)
    }
}

fun Job.plan(init: PlanWrapper.() -> Unit) {
    PlanWrapper(this).init()
}

class PlanWrapper(val job: Job) {
    fun get(resource: String, init: Step.GetStep.() -> Unit): Step.GetStep {
        val step = Step.GetStep(resource)
        step.init()
        job.plan.add(step)
        return step
    }

    fun task(name: String, init: Step.TaskStep.() -> Unit): Step.TaskStep {
        val step = Step.TaskStep(name)
        step.init()
        job.plan.add(step)
        return step
    }
}

