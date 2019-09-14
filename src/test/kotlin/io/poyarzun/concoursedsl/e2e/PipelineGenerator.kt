package io.poyarzun.concoursedsl.e2e

import fr.xgouchet.elmyr.Forger
import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslMap
import io.poyarzun.concoursedsl.dsl.DslObject
import kotlin.math.absoluteValue
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class PipelineGenerator(private val forger: Forger) {
    fun generateRandomPipeline(): Pipeline {
        return generateObject(Pipeline::class, 0)
    }

    private fun <T : Any> generateObject(klass: KClass<T>, depth: Int): T {
        return when {
            klass.isSubclassOf(String::class) -> forger.aWord() as T
            klass.isSubclassOf(Boolean::class) -> forger.aBool() as T
            klass.isSubclassOf(Int::class) -> forger.aSmallInt() as T
            klass == Step::class -> {
                return forger.anElementFrom(
                        generateObject(GenericGetStep::class, depth + 1) as T,
                        generateObject(GenericPutStep::class, depth + 1) as T,
                        generateObject(Step.TaskStep::class, depth + 1) as T
                )
            }
            else -> {
                val params = mutableListOf<Any>()
                val constructor = klass.primaryConstructor
                        ?: throw IllegalStateException("No primary constructor for $klass")
                constructor.parameters.forEach {
                    val paramValue = generateObject(it.type.classifier as KClass<Any>, depth + 1)
                    params.add(paramValue)
                }
                val obj = constructor.call(*params.toTypedArray())
                klass.memberProperties.forEach {
                    if (depth > 10) {
                        return@forEach
                    }
                    if (it.returnType.isMarkedNullable && !forger.aBool(0.5f)) {
                        return@forEach
                    }
                    val returnType = it.returnType.classifier as KClass<Any>
                    when {
                        returnType.isSubclassOf(DslList::class) -> fillInListProp(klass, it.name, it.get(obj) as DslList<Any>, depth + 1)
                        returnType.isSubclassOf(DslObject::class) -> fillInObjectProp(klass, it.name, it.get(obj) as DslObject<Any>, depth + 1)
                        returnType.isSubclassOf(DslMap::class) -> fillInMapProp(klass, it.name, it.get(obj) as DslMap<Any, Any?>)
                        it is KMutableProperty<*> -> {
                            val propValue = generateObject(returnType, depth + 1)
                            it.setter.call(obj, propValue)
                        }
                    }
                }
                return obj
            }
        }
    }

    private fun <T : Any, K : Any> fillInListProp(parent: KClass<T>, name: String, list: DslList<K>, depth: Int) {
        mappings.forEach {
            if (parent.isSubclassOf(it.parent) && name == it.name) {
                list.generateList(it.elementType as KClass<K>, depth + 1)
                return
            }
        }
        throw IllegalStateException("$parent.$name")
    }

    private fun <T : Any> fillInMapProp(parent: KClass<T>, name: String, map: DslMap<Any, Any?>) {
        var valueGenerator: () -> Any? = { fillInLiteral() }
        mappings.forEach { mapping: Mapping<Any, Any> ->
            if (parent.isSubclassOf(mapping.parent) && name == mapping.name) {
                if (mapping.elementType.isSubclassOf(String::class)) {
                    valueGenerator = { forger.aWord() }
                }
            }
        }
        val numElements = forger.anInt(min = 0, max = 3)
        repeat(numElements) {
            map[forger.aWord()] = valueGenerator()
        }
    }

    private fun fillInLiteral(depth: Int = 0): Any? {
        if (depth > 3) return null
        val fl = forger.aGaussianFloat(mean = 0f, standardDeviation = 0.5f).absoluteValue
        return when {
            fl < 0.2 -> forger.aWord()
            fl < 0.4 -> forger.aBool()
            fl < 0.6 -> forger.aSmallInt()
            fl < 0.8 && depth == 0 -> forger.aList(2) { fillInLiteral(depth + 1) }
            else -> null
        }
    }

    private fun <T : Any, K : Any> fillInObjectProp(parent: KClass<T>, name: String, obj: DslObject<K>, depth: Int) {
        mappings.forEach {
            if (parent.isSubclassOf(it.parent) && name == it.name) {
                obj.value = generateObject(it.elementType as KClass<K>, depth + 1)
                return
            }
        }
        throw IllegalStateException("$parent.$name")
    }

    private fun <T : Any> DslList<T>.generateList(kClass: KClass<T>, depth: Int) {
        val numberOfElements = forger.anInt(min = 0, max = 8)
        repeat(numberOfElements) {
            +generateObject(kClass, depth + 1)
        }
    }

    data class Mapping<T : Any, K : Any>(
            val parent: KClass<T>,
            val name: String,
            val elementType: KClass<K>
    )

    private val mappings = listOf(
            Mapping(Pipeline::class as KClass<Any>, "groups", Group::class as KClass<Any>),
            Mapping(Pipeline::class as KClass<Any>, "jobs", Job::class as KClass<Any>),
            Mapping(Pipeline::class as KClass<Any>, "resourceTypes", ResourceType::class as KClass<Any>),
            Mapping(Pipeline::class as KClass<Any>, "resources", GenericResource::class as KClass<Any>),
            Mapping(Job::class as KClass<Any>, "plan", Step::class as KClass<Any>),
            Mapping(Job::class as KClass<Any>, "serialGroups", String::class as KClass<Any>),
            Mapping(GenericResource::class as KClass<Any>, "tags", String::class as KClass<Any>),
            Mapping(GenericResource::class as KClass<Any>, "version", String::class as KClass<Any>),
            Mapping(GenericGetStep::class as KClass<Any>, "passed", String::class as KClass<Any>),
            Mapping(GenericGetStep::class as KClass<Any>, "tags", String::class as KClass<Any>),
            Mapping(GenericPutStep::class as KClass<Any>, "tags", String::class as KClass<Any>),
            Mapping(ResourceType::class as KClass<Any>, "tags", String::class as KClass<Any>),
            Mapping(Group::class as KClass<Any>, "jobs", String::class as KClass<Any>),
            Mapping(Group::class as KClass<Any>, "resources", String::class as KClass<Any>),
            Mapping(Step.TaskStep::class as KClass<Any>, "config", Task::class as KClass<Any>),
            Mapping(Step.TaskStep::class as KClass<Any>, "tags", String::class as KClass<Any>),
            Mapping(Step.TaskStep::class as KClass<Any>, "inputMapping", String::class as KClass<Any>),
            Mapping(Step.TaskStep::class as KClass<Any>, "outputMapping", String::class as KClass<Any>),
            Mapping(Task::class as KClass<Any>, "imageResource", Task.Resource::class as KClass<Any>),
            Mapping(Task::class as KClass<Any>, "inputs", Task.Input::class as KClass<Any>),
            Mapping(Task::class as KClass<Any>, "outputs", Task.Output::class as KClass<Any>),
            Mapping(Task::class as KClass<Any>, "run", Task.RunConfig::class as KClass<Any>),
            Mapping(Task.RunConfig::class as KClass<Any>, "args", String::class as KClass<Any>),
            Mapping(Task.Resource::class as KClass<Any>, "version", String::class as KClass<Any>),
            Mapping(Task::class as KClass<Any>, "caches", Task.Cache::class as KClass<Any>)
    )
}