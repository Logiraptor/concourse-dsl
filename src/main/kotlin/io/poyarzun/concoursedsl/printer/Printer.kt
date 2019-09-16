package io.poyarzun.concoursedsl.printer

import com.squareup.kotlinpoet.*
import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslMap
import io.poyarzun.concoursedsl.dsl.DslObject
import io.poyarzun.concoursedsl.dsl.readYML
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class Printer {
    private val nameAllocator = NameAllocator()

    fun convertYamlToDsl(yaml: String): String {
        val pipeline = readYML(yaml)

        val file = FileSpec.builder("pipeline", "Main")
                .addImport("io.poyarzun.concoursedsl.domain",
                        "pipeline", "inParallel",
                        "group", "aggregate", "cache", "input", "output",
                        "task", "do", "resourceType", "try",
                        "job", "resource", "put", "get"
                )
                .addImport("io.poyarzun.concoursedsl.dsl", "generateYML")
                .pipelineToSource(pipeline)
                .addFunction(FunSpec.builder("main")
                        .addStatement("println(generateYML(mainPipeline()))")
                        .build())
                .build()

        return StringBuilder().apply {
            file.writeTo(this)
        }.toString()
    }

    private fun FileSpec.Builder.pipelineToSource(pipeline: Pipeline): FileSpec.Builder {
        val jobNames = pipeline.jobs.map { nameAllocator.newName(it.name) }
        pipeline.jobs.forEachIndexed { i, it ->
            addFunction(FunSpec.builder(jobNames[i]).addStatement("return %L", buildCodeBlock {
                generateConstructorDsl(typeToName(Job::class), it)
            }).build())
        }
        val resourceNames = pipeline.resources.map { nameAllocator.newName(it.name) }
        pipeline.resources.forEachIndexed { i, it ->
            addFunction(FunSpec.builder(resourceNames[i]).addStatement("return %L", buildCodeBlock {
                generateConstructorDsl(typeToName(GenericResource::class), it)
            }).build())
        }
        val resourceTypeNames = pipeline.resourceTypes.map { nameAllocator.newName(it.name) }
        pipeline.resourceTypes.forEachIndexed { i, it ->
            addFunction(FunSpec.builder(resourceTypeNames[i]).addStatement("return %L", buildCodeBlock {
                generateConstructorDsl(typeToName(ResourceType::class), it)
            }).build())
        }

        return addFunction(FunSpec.builder("mainPipeline")
                .beginControlFlow("return pipeline")
                .generatePlusFunctionCalls(jobNames, "jobs")
                .generatePlusFunctionCalls(resourceNames, "resources")
                .generatePlusFunctionCalls(resourceTypeNames, "resourceTypes")
                .addCode(buildCodeBlock {
                    generatePropertyDsl("groups", pipeline.groups)
                })
                .endControlFlow()
                .build())
    }

    private fun FunSpec.Builder.generatePlusFunctionCalls(names: List<String>, name: String): FunSpec.Builder {
        beginControlFlow(name)
        names.forEach {
            addStatement("+%N()", it)
        }
        return endControlFlow()
    }

    private fun <T : Any> CodeBlock.Builder.generateConstructorDsl(name: String, value: T): CodeBlock.Builder {

        when (value) {
            is Step.InParallelStep -> {
                return beginControlFlow("inParallel")
                        .generateMemberPropertiesExcept(Step.InParallelStep.InParallelConfig::class, emptyList(), value.inParallel)
                        .generateMemberPropertiesExcept(Step.InParallelStep::class, listOf("inParallel"), value)
                        .endControlFlow()
            }
            is Step.AggregateStep -> {
                return beginControlFlow("aggregate")
                        .generatePlusDsl(value.aggregate)
                        .generateMemberPropertiesExcept(Step.AggregateStep::class, listOf("aggregate"), value)
                        .endControlFlow()
            }
            is Step.DoStep -> {
                return beginControlFlow("`do`")
                        .generatePlusDsl(value.`do`)
                        .generateMemberPropertiesExcept(Step.DoStep::class, listOf("do"), value)
                        .endControlFlow()
            }
            is Step.TryStep -> {
                return beginControlFlow("`try`")
                        .generateMemberPropertiesExcept(Step.TryStep::class, listOf("try"), value)
                        .generateConstructorDsl(typeToName(value.`try`.javaClass.kotlin), value.`try`)
                        .endControlFlow()
            }
        }

        val type = value.javaClass.kotlin
        val params = getRequiredConstructorParameters(value, type)

        val argList = params.joinToString(", ") { it.template }
        val paramTemplate = when {
            argList.isNotEmpty() -> "($argList)"
            else -> ""
        }
        val args = params.map { it.value }.toTypedArray()
        val argNames = params.map { it.name }
        return beginControlFlow("$name$paramTemplate", *args)
                .generateMemberPropertiesExcept(type, argNames, value)
                .endControlFlow()
    }

    private fun <T : Any> CodeBlock.Builder.generateMemberPropertiesExcept(type: KClass<T>, params: List<String?>, value: T): CodeBlock.Builder {
        type.memberProperties.forEach { prop ->
            val filtered = params.any { it == prop.name }
            if (filtered) {
                return@forEach
            }
            val classifier = prop.returnType.classifier
            if (classifier is KClass<*>) {
                val propValue = prop.get(value) ?: return@forEach

                generatePropertyDsl(prop.name, propValue)
            }
        }
        return this
    }

    private fun <T : Any> CodeBlock.Builder.generatePropertyDsl(name: String, value: T): CodeBlock.Builder {
        return when (value) {
            is String -> addStatement("$name = %S", value)
            is Int -> addStatement("$name = %L", value)
            is Boolean -> addStatement("$name = %L", value)
            is DslMap<*, *> -> {
                if (value.isEmpty()) return this
                beginControlFlow(name)
                value.forEach {
                    addStatement("this[%S] = %L", it.key, generateLiteral(it.value))
                }
                endControlFlow()
            }
            is DslList<*> -> {
                if (value.isEmpty()) return this
                if (value.all { it is String }) {
                    val totalLength = value.sumBy { (it as String).length }
                    val hasNewLines = value.any { (it as String).contains('\n') }
                    if (totalLength < 100 && !hasNewLines) {
                        return add(value.map { buildCodeBlock { add("%S", it) } }.joinToCode(prefix = "$name(", suffix = ")\n"))
                    }
                }

                return beginControlFlow(name)
                        .generatePlusDsl<T>(value)
                        .endControlFlow()
            }
            is DslObject<*> -> {
                val innerVal = value.value ?: return this
                generateConstructorDsl(name, innerVal)
            }
            else -> {
                add("$name = %L", buildCodeBlock { generateConstructorDsl(typeToName(value.javaClass.kotlin), value) })
            }
        }
    }

    private fun <T : Any> CodeBlock.Builder.generatePlusDsl(value: T): CodeBlock.Builder {
        (value as MutableList<Any>).forEach {
            when (it) {
                is String -> addStatement("+%S", it)
                else -> add("+%L", buildCodeBlock { generateConstructorDsl(typeToName(it.javaClass.kotlin), it) })
            }
        }
        return this
    }

    data class ConfiguredContructorArg(
            val name: String?,
            val template: String,
            val value: Any
    )

    private fun <T : Any> getRequiredConstructorParameters(value: T, type: KClass<T>): MutableList<ConfiguredContructorArg> {
        val params = mutableListOf<ConfiguredContructorArg>()

        val primaryConstructor = type.primaryConstructor ?: return params

        primaryConstructor.parameters.forEach { param ->
            if (!param.isOptional) {
                val backingPropertyValue = value.lookupProperty(param.name)

                if (backingPropertyValue == null) {
                    params.add(ConfiguredContructorArg(
                            name = param.name,
                            template = "TODO(%S)",
                            value = "Could not determine value for required parameter: ${param.name}"
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
        return params
    }

    private fun <T : Any> typeToName(type: KClass<T>): String {
        return when {
            type.isSubclassOf(Pipeline::class) -> "pipeline"
            type.isSubclassOf(Group::class) -> "group"
            type.isSubclassOf(Job::class) -> "job"
            type.isSubclassOf(Step.AggregateStep::class) -> "aggregate"
            type.isSubclassOf(Step.InParallelStep::class) -> "inParallel"
            type.isSubclassOf(GenericGetStep::class) -> "get"
            type.isSubclassOf(GenericPutStep::class) -> "put"
            type.isSubclassOf(Step.TaskStep::class) -> "task"
            type.isSubclassOf(Step.DoStep::class) -> "`do`"
            type.isSubclassOf(Step.TryStep::class) -> "`try`"
            type.isSubclassOf(ResourceType::class) -> "resourceType"
            type.isSubclassOf(GenericResource::class) -> "resource"
            type.isSubclassOf(Task.Cache::class) -> "cache"
            type.isSubclassOf(Task.Input::class) -> "input"
            type.isSubclassOf(Task.Output::class) -> "output"
            else -> TODO("typeToName($type)")
        }
    }

    private fun generateLiteral(value: Any?): CodeBlock {
        return buildCodeBlock {
            when (value) {
                is String -> add("%S", value)
                is List<*> -> add(value.map(::generateLiteral).joinToCode(prefix = "listOf(", suffix = ")"))
                is Int -> add("%L", value)
                is Boolean -> add("%L", value)
                is Map<*, *> -> add(value.map {
                    buildCodeBlock {
                        add("%S to %L", it.key, generateLiteral(it.value))
                    }
                }.joinToCode(prefix = "mapOf(", suffix = ")"))
                else -> when (value) {
                    null -> add("null")
                    else -> add("TODO(%S)", "Cannot generate dsl for literal: $value")
                }
            }
        }
    }
}

private fun <T : Any> T.lookupProperty(param: String?): Any? {
    javaClass.kotlin.members.forEach {
        if (it is KProperty<*> && it.name == param) {
            return it.getter.call(this)
        }
    }
    return null
}
