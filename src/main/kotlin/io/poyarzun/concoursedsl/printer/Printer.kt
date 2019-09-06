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

object Printer {
    fun convertYamlToDsl(yaml: String): String {
        val pipeline = readYML(yaml)

        val file = FileSpec.builder("pipeline", "Main")
                .addImport("io.poyarzun.concoursedsl.domain",
                        "pipeline",
                        "group", "aggregate", "cache", "input", "output",
                        "task", "do", "resourceType", "try",
                        "job", "resource", "put", "get"
                )
                .addImport("io.poyarzun.concoursedsl.dsl", "generateYML")
                .addFunction(pipelineToSource(pipeline))
                .addFunction(FunSpec.builder("main")
                        .addStatement("println(generateYML(mainPipeline()))")
                        .build())
                .build()

        return StringBuilder().apply {
            file.writeTo(this)
        }.toString()
    }

    private fun pipelineToSource(pipeline: Pipeline): FunSpec {
        return FunSpec.builder("mainPipeline")
                .addStatement("return %L", generateDsl(pipeline, Pipeline::class))
                .build()
    }

    private fun <T : Any> generateDsl(value: T, type: KClass<T>) = buildCodeBlock {
        generateConstructorDsl("", "", typeToName(type), value)
    }
    private fun <T : Any> CodeBlock.Builder.generateConstructorDsl(prefix: String, suffix: String, name: String, value: T): CodeBlock.Builder {

        when (value) {
            is Step.AggregateStep -> return generatePropertyDsl(prefix, suffix, "aggregate", value.aggregate)
            is Step.DoStep -> return generatePropertyDsl(prefix, suffix, "`do`", value.`do`)
            is Step.TryStep -> return add("$prefix`try`(%L)$suffix", generateDsl(value.`try`, value.`try`.javaClass.kotlin))
        }

        val type = value.javaClass.kotlin
        val params = getRequiredConstructorParameters(value, type)

        val argList = params.joinToString(", ") { it.template }
        val paramTemplate = when {
            argList.isNotEmpty() -> "($argList)"
            else -> ""
        }
        val args = params.map { it.value }.toTypedArray()
        beginControlFlow("$prefix$name$paramTemplate$suffix", *args)
        type.memberProperties.forEach { prop ->
            val specifiedInConstructor = params.any { it.name == prop.name }
            if (specifiedInConstructor) {
                return@forEach
            }
            val classifier = prop.returnType.classifier
            if (classifier is KClass<*>) {
                val propValue = prop.get(value) ?: return@forEach

                generatePropertyDsl("", "", prop.name, propValue)
            }
        }
        return endControlFlow()
    }


    private fun <T : Any> CodeBlock.Builder.generatePropertyDsl(prefix: String, suffix: String, name: String, value: T): CodeBlock.Builder {
        return when (value) {
            is String -> addStatement("$name = %S", value)
            is Int -> addStatement("$name = %L", value)
            is Boolean -> addStatement("$name = %L", value)
            is DslMap<*, *> -> {
                if (value.isEmpty()) return this
                beginControlFlow(name)
                value.forEach {
                    addStatement("put(%S, %L)", it.key, generateLiteral(it.value))
                }
                endControlFlow()
            }
            is DslList<*> -> {
                if (value.isEmpty()) return this
                beginControlFlow(prefix + name + suffix)
                (value as DslList<Any>).forEach {
                    when (it) {
                        is String -> addStatement("+%S", it)
                        else -> generateConstructorDsl("+", "", typeToName(it.javaClass.kotlin), it)
                    }
                }
                endControlFlow()
            }
            is DslObject<*> -> {
                val innerVal = value.value ?: return this
                generateConstructorDsl(prefix, suffix, name, innerVal)
            }
            else -> {
                generateConstructorDsl("$name = ", "", typeToName(value.javaClass.kotlin), value)
            }
        }
    }

    fun generateLiteral(value: Any?): CodeBlock {
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
                val backingPropertyValue = findPropertyValue(value, type, param.name)

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

    private fun <T : Any> findPropertyValue(value: T, type: KClass<T>, param: String?): Any? {
        type.members.forEach {
            if (it is KProperty<*> && it.name == param) {
                return it.getter.call(value)
            }
        }
        return null
    }
}

