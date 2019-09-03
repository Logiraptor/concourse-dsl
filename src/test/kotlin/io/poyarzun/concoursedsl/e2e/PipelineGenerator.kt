package io.poyarzun.concoursedsl.e2e

import fr.xgouchet.elmyr.Forger
import io.poyarzun.concoursedsl.domain.*
import io.poyarzun.concoursedsl.dsl.DslList
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class PipelineGenerator(private val forger: Forger) {
    fun generateRandomPipeline(): Pipeline {
        return generateObject(Pipeline::class)
    }

    private fun <T : Any> generateObject(klass: KClass<T>): T {
        return when {
            klass.isSubclassOf(String::class) -> forger.aWord() as T
            klass.isSubclassOf(Boolean::class) -> forger.aBool() as T
            klass.isSubclassOf(Int::class) -> forger.aSmallInt() as T
            klass.isSubclassOf(Step::class) && !klass.isSubclassOf(GenericGetStep::class) -> {
                return generateObject(GenericGetStep::class) as T
            }
            else -> {
                val params = mutableListOf<Any>()
                val constructor = klass.primaryConstructor
                        ?: throw IllegalStateException("No primary constructor for $klass")
                constructor.parameters.forEach {
                    val paramValue = generateObject(it.type.classifier as KClass<Any>)
                    params.add(paramValue)
                }
                val obj = constructor.call(*params.toTypedArray())
                klass.memberProperties.forEach {
                    if (it.returnType.isMarkedNullable && !forger.aBool(0.1f)) {
                        return@forEach
                    }
                    val returnType = it.returnType.classifier as KClass<Any>
                    if (returnType.isSubclassOf(DslList::class)) {
                        fillInListProp(klass, it.name, it.get(obj) as DslList<Any>)
                    } else if (it is KMutableProperty<*>) {
                        val propValue = generateObject(returnType)
                        it.setter.call(obj, propValue)
                    }
                }
                return obj
            }
        }
    }

    private fun <T : Any, K : Any> fillInListProp(parent: KClass<T>, name: String, list: DslList<K>) {
        data class Mapping(
                val parent: KClass<T>,
                val name: String,
                val elementType: KClass<K>
        )

        val mappings = listOf(
                Mapping(Pipeline::class as KClass<T>, "groups", Group::class as KClass<K>),
                Mapping(Pipeline::class as KClass<T>, "jobs", Job::class as KClass<K>),
                Mapping(Pipeline::class as KClass<T>, "resourceTypes", ResourceType::class as KClass<K>),
                Mapping(Pipeline::class as KClass<T>, "resources", GenericResource::class as KClass<K>),
                Mapping(Job::class as KClass<T>, "plan", Step::class as KClass<K>),
                Mapping(Job::class as KClass<T>, "serialGroups", String::class as KClass<K>),
                Mapping(GenericResource::class as KClass<T>, "tags", String::class as KClass<K>),
                Mapping(GenericGetStep::class as KClass<T>, "passed", String::class as KClass<K>),
                Mapping(GenericGetStep::class as KClass<T>, "tags", String::class as KClass<K>),
                Mapping(ResourceType::class as KClass<T>, "tags", String::class as KClass<K>),
                Mapping(Group::class as KClass<T>, "jobs", String::class as KClass<K>),
                Mapping(Group::class as KClass<T>, "resources", String::class as KClass<K>)
        )
        mappings.forEach {
            if (parent.isSubclassOf(it.parent) && name == it.name) {
                list.generateList(it.elementType)
                return
            }
        }
        throw IllegalStateException("$parent.$name")
    }

    private fun <T  : Any> DslList<T>.generateList(kClass: KClass<T>) {
        val numberOfElements = forger.anInt(min = 0, max = 8)
        repeat(numberOfElements) {
            +generateObject(kClass)
        }
    }
}