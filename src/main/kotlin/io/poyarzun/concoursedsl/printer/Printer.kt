package io.poyarzun.concoursedsl.printer

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.exhaustivePipeline
import java.io.File
import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor


fun main() {
    val p = exhaustivePipeline

    val file = FileSpec.builder("pipeline", "Main")
            .addImport("io.poyarzun.concoursedsl.dsl",
                    "pipeline",
                    "inputMapping",
                    "resource", "put", "plan",
                    "params", "config", "imageResource",
                    "source", "job", "get", "getParams",
                    "outputMapping", "passed", "serialGroups",
                    "ensure", "onAbort", "onSuccess", "onFailure",
                    "input", "output", "tags", "version", "cache"
            )
            .addFunction(pipelineToSource(p))
            .addFunction(FunSpec.builder("main")
                    .addStatement("println(mainPipeline())")
                    .build())
            .build()

    val f = File("printer-test/src/main/kotlin")
    f.deleteRecursively()
    file.writeTo(f)
}


fun pipelineToSource(pipeline: Pipeline): FunSpec {
    return FunSpec.builder("mainPipeline")
            .beginControlFlow("return pipeline")
            .resources(pipeline.resources)
            .jobs(pipeline.jobs)
            .endControlFlow()
            .build()
}

fun FunSpec.Builder.resources(resources: MutableList<Resource<Any>>): FunSpec.Builder {
    fun FunSpec.Builder.resourceToSource(resource: Resource<Any>) {
        beginControlFlow("resource(%S, %S)", resource.name, resource.type)
                .beginControlFlow("source")
                .configurePlainObject(resource.source)
                .endControlFlow()
                .configurePlainObject(resource)
                .endControlFlow()
    }

    resources.forEach(::resourceToSource)
    return this
}

fun FunSpec.Builder.jobs(jobs: MutableList<Job>): FunSpec.Builder {
    fun FunSpec.Builder.jobToSource(job: Job) {
        autoConfigure("job", job, Job::class)
//        beginControlFlow("job(%S)", job.name)
//                .beginControlFlow("plan")
//                .steps(job.plan)
//                .endControlFlow()
//                .configurePlainObject(job)
//                .endControlFlow()
    }

    jobs.forEach(::jobToSource)
    return this
}

fun FunSpec.Builder.steps(steps: MutableList<Step>): FunSpec.Builder {
    fun FunSpec.Builder.stepToSource(step: Step) {
        when (step) {
            is Step.GetStep<*> -> {
                beginControlFlow("get(%S)", step.get)
                        .beginControlFlow("params")
                        .configurePlainObject(step.params)
                        .endControlFlow()
                        .configurePlainObject(step)
                        .endControlFlow()
            }
            is Step.PutStep<*, *> -> {
                beginControlFlow("put(%S)", step.put)
                        .beginControlFlow("params")
                        .configurePlainObject(step.params)
                        .endControlFlow()
                        .beginControlFlow("getParams")
                        .configurePlainObject(step.getParams)
                        .endControlFlow()
                        .configurePlainObject(step)
                        .endControlFlow()
            }
            is Step.TaskStep -> {
                beginControlFlow("task(%S)", step.task)
                        .configurationBlock("inputMapping", step.inputMapping)
                        .configurationBlock("outputMapping", step.outputMapping)
                        .configurationBlock("config(%S, %S)", step.config, { it.platform }, { it.run.path })
                        .configurePlainObject(step, "config")
                        .endControlFlow()
            }
        }
    }

    steps.forEach(::stepToSource)
    return this
}

inline fun <reified T : Any> FunSpec.Builder.configurationBlock(name: String, value: T?, vararg args: (T) -> Any): FunSpec.Builder {
    if (value == null) {
        return this
    }

    return beginControlFlow(name, *(args.map { it(value) }.toTypedArray()))
            .configurePlainObject(value)
            .endControlFlow()
}


inline fun <reified T : Any> FunSpec.Builder.configurePlainObject(value: T, vararg skipList: String) =
        configurePlainObject(value, T::class, skipList)


// configurePlainObject emits an assignment for each mutable property in value
fun <T : Any> FunSpec.Builder.configurePlainObject(value: T, type: KClass<T>, skipList: Array<out String>): FunSpec.Builder {
    when (value) {
        is Map<*, *> -> {
            value.forEach {
                val key = it.key
                val value = it.value
                when (key) {
                    is String ->
                        when (value) {
                            is String ->
                                addStatement("put(%S, %S)", key, value)
                            else ->
                                addStatement("put(%S, TODO(%S))", key, "Could not automatically set value: $value")
                        }
                    else ->
                        when (value) {
                            is String ->
                                addStatement("put(TODO(%S), %S)", "Could not automatically set value: $key", value)
                            else ->
                                addStatement("put(TODO(%S), TODO(%S))", "Could not automatically set value: $key", "Could not automatically set value: $value")
                        }
                }
            }
        }
        else -> {
            if (type == Any::class) {
                addStatement("TODO(%S)", "Could not automatically configurePlainObject ${value.javaClass}")
                return this
            }
            type.declaredMemberProperties.forEach { prop: KProperty1<T, *> ->
                if (prop !is KMutableProperty1<T, *> || skipList.contains(prop.name)) return@forEach

                val propValue = prop.get(value)
                if (propValue != null) {
                    when (propValue) {
                        is String ->
                            addStatement("${prop.name} = %S", propValue)
                        is Int ->
                            addStatement("${prop.name} = %L", propValue)
                        is Boolean ->
                            addStatement("${prop.name} = %L", propValue)
                        is List<*> -> {
                            beginControlFlow(prop.name)
                            propValue.forEach {
                                if (it !is String) {
                                    addStatement("TODO(%S)", "Could not automatically generate type ${it?.javaClass}")
                                } else {
                                    addStatement("add(%S)", it)
                                }
                            }
                            endControlFlow()
                        }
                        else -> {
                            val kClassifier = prop.returnType.classifier
                            println("decending: $kClassifier")
                            if (kClassifier is KClass<*>) {
                                val primaryConstructor = kClassifier.primaryConstructor
                                if (primaryConstructor is KFunction<*>) {
                                    println(primaryConstructor.parameters.map { "${it.name} - ${it.isOptional}" })
                                }
                            }
                            configurationBlock(prop.name, propValue)
                        }
                    }
                }
            }
        }
    }

    return this
}

fun <T : Any> FunSpec.Builder.autoConfigure(name: String, value: T, type: KClass<T>) {
    when {
        type.isSubclassOf(Int::class) -> addStatement("$name = %L", value)
        type.isSubclassOf(Boolean::class) -> addStatement("$name = %L", value)
        type.isSubclassOf(String::class) -> addStatement("$name = %S", value)
        type.isSubclassOf(MutableList::class) -> autoConfigureList(name, value)
        type.isSubclassOf(MutableMap::class) -> {
            beginControlFlow(name)
            (value as MutableMap<String, Any>).forEach { key, value ->
                when (value) {
                    is Int -> addStatement("put(%S, %L)", key, value)
                    is Boolean -> addStatement("put(%S, %L)", key, value)
                    is String -> addStatement("put(%S, %S)", key, value)
                }
            }
            endControlFlow()
        }
        type.declaredMemberProperties.isNotEmpty() -> autoConfigureDeclaredMemberProperties(value, type, name)
        else -> addStatement("TODO(%S)", "Cannot autoconfigure $type")
    }
}

private fun <T : Any> FunSpec.Builder.autoConfigureList(name: String, value: T) {
    beginControlFlow(name)
    (value as MutableList<Any>).forEach {
        when (it) {
            is String -> addStatement("add(%S)", it)
            is Step.GetStep<*> -> autoConfigure("get", it, Step.GetStep::class)
            is Step.PutStep<*, *> -> autoConfigure("put", it, Step.PutStep::class)
            is Step.TaskStep -> autoConfigure("task", it, Step.TaskStep::class)

            is Step.AggregateStep -> autoConfigure("aggregate", it, Step.AggregateStep::class)
            is Step.TryStep -> autoConfigure("`try`", it, Step.TryStep::class)
            is Step.DoStep -> autoConfigure("`do`", it, Step.DoStep::class)

            is Task.Input -> autoConfigure("input", it, Task.Input::class)
            is Task.Cache -> autoConfigure("cache", it, Task.Cache::class)
            is Task.Output -> autoConfigure("output", it, Task.Output::class)
            else -> addStatement("TODO(%S)", "Could not add to list (${it.javaClass}")
        }
    }
    endControlFlow()
}

private fun <T : Any> FunSpec.Builder.autoConfigureDeclaredMemberProperties(value: T, type: KClass<T>, name: String) {
    val params = getRequiredConstructorParameters(value, type)

    val paramTemplate = params.joinToString(", ") { it.template }
    beginControlFlow("$name($paramTemplate)", *(params.map { it.value }.toTypedArray()))

    type.declaredMemberProperties.forEach {

        if (params.any { param -> param.name == it.name }) {
            return@forEach
        }

        val classifier = it.returnType.classifier
        if (classifier is KClass<*>) {
            val propValue = it.get(value) ?: return@forEach

            autoConfigure(it.name, propValue, classifier as KClass<Any>)
        }
    }
    endControlFlow()
}

data class ConfiguredContructorArg(
        val name: String?,
        val template: String,
        val value: Any
)

fun <T : Any> getRequiredConstructorParameters(value: T, type: KClass<T>): MutableList<ConfiguredContructorArg> {
    val params = mutableListOf<ConfiguredContructorArg>()

    val primaryConstructor = type.primaryConstructor ?: return params

    primaryConstructor.parameters.forEach { param ->
        if (!param.isOptional) {
            val backingProperty = type.declaredMemberProperties.find { it.name == param.name }

            if (backingProperty == null) {
                params.add(ConfiguredContructorArg(
                        name = param.name,
                        template = "TODO(%S)",
                        value = "Could not determine value for required parameter: ${param.name}"
                ))
            } else {
                val backingPropertyValue = backingProperty.get(value)
                if (backingPropertyValue == null) {
                    params.add(ConfiguredContructorArg(
                            name = param.name,
                            template = "TODO(%S)",
                            value = "Could not insert null value for required parameter: ${param.name}"
                    ))
                } else {
                    params.add(ConfiguredContructorArg(
                            name = param.name,
                            template = "%S",
                            value = backingPropertyValue
                    ))
                }
            }
        }
    }
    return params
}
