package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.*

fun Job.plan(configBlock: ConfigBlock<StepBuilder>) {
    StepBuilder(this.plan::add).apply(configBlock)
}

fun <SourceProps : Any> Resource<SourceProps>.source(configBlock: ConfigBlock<SourceProps>) {
    this.source.apply(configBlock)
}

fun <InProps : Any> Step.GetStep<InProps>.params(configBlock: ConfigBlock<InProps>) {
    this.params.apply(configBlock)
}

fun <InProps : Any, OutProps : Any> Step.PutStep<InProps, OutProps>.params(configBlock: ConfigBlock<OutProps>) {
    this.params.apply(configBlock)
}

fun <InProps : Any, OutProps : Any> Step.PutStep<InProps, OutProps>.getParams(configBlock: ConfigBlock<InProps>) {
    this.getParams.apply(configBlock)
}

fun Step.TaskStep.inputMapping(configBlock: ConfigBlock<MutableMap<String, String>>) {
    inputMapping.apply(configBlock)
}

fun Step.TaskStep.outputMapping(configBlock: ConfigBlock<MutableMap<String, String>>) {
    outputMapping.apply(configBlock)
}

fun Step.TaskStep.config(platform: String, path: String, configBlock: ConfigBlock<Task>) {
    config = Task(platform, Task.RunConfig(path)).apply(configBlock)
}

fun Task.run(configBlock: ConfigBlock<Task.RunConfig>) {
    run.apply(configBlock)
}

fun ResourceType.source(configBlock: ConfigBlock<Source>) {
    source.apply(configBlock)
}

fun ResourceType.params(configBlock: ConfigBlock<Params>) {
    params.apply(configBlock)
}

fun <T: Any> Step.GetStep<T>.passed(configBlock: ConfigBlock<MutableList<String>>) {
    passed = mutableListOf<String>().apply(configBlock)
}

fun Job.serialGroups(configBlock: ConfigBlock<MutableList<String>>) {
    serialGroups = mutableListOf<String>().apply(configBlock)
}

fun Step.TaskStep.params(configBlock: ConfigBlock<Params>) {
    params = mutableMapOf<String, Any?>().apply(configBlock)
}

fun Task.imageResource(type: String, configBlock: ConfigBlock<Task.Resource>) {
    imageResource = Task.Resource(type).apply(configBlock)
}

fun Task.RunConfig.args(configBlock: ConfigBlock<MutableList<String>>) {
    args = mutableListOf<String>().apply(configBlock)
}


fun Task.Resource.source(configBlock: ConfigBlock<Source>) {
    source.apply(configBlock)
}

fun Task.Resource.params(configBlock: ConfigBlock<Params>) {
    params = mutableMapOf<String, Any?>().apply(configBlock)
}

fun Task.Resource.version(configBlock: ConfigBlock<Version>) {
    version = mutableMapOf<String, String>().apply(configBlock)
}

fun <T: Any> Resource<T>.version(configBlock: ConfigBlock<Version>) {
    version.apply(configBlock)
}

fun Task.input(name: String, configBlock: ConfigBlock<Task.Input>) {
    inputs.add(Task.Input(name).apply(configBlock))
}

fun Task.output(name: String, configBlock: ConfigBlock<Task.Output>) {
    outputs.add(Task.Output(name).apply(configBlock))
}

fun Task.cache(name: String) {
    caches.add(Task.Cache(name))
}

class TagsDsl(private val tags: MutableList<String>) {
    operator fun String.unaryPlus() {
        tags.add(this)
    }
}

fun ResourceType.tags(configBlock: ConfigBlock<TagsDsl>) {
    TagsDsl(tags).apply(configBlock)
}

fun <T: Any> Resource<T>.tags(configBlock: ConfigBlock<TagsDsl>) {
    TagsDsl(tags).apply(configBlock)
}

fun Step.tags(configBlock: ConfigBlock<TagsDsl>) {
    TagsDsl(tags).apply(configBlock)
}
