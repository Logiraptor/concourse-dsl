package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import io.poyarzun.concoursedsl.domain.*
import java.io.IOException
import kotlin.reflect.KClass


object DslModule : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.addBeanSerializerModifier(DslBeanSerializerModifier)
    }

    object DslBeanSerializerModifier : BeanSerializerModifier() {
        override fun modifySerializer(config: SerializationConfig?, desc: BeanDescription, serializer: JsonSerializer<*>): JsonSerializer<*> {
            return when {
                DslObject::class.java.isAssignableFrom(desc.beanClass) -> DslObjectSerializer(serializer as JsonSerializer<Any>)
                else -> serializer
            }
        }
    }

    // This serializer customizes DslObject to be excluded from the yaml if the internal value is null
    class DslObjectSerializer(private val defaultSerializer: JsonSerializer<Any>) : JsonSerializer<DslObject<Any>>() {

        @Throws(IOException::class, JsonProcessingException::class)
        override fun serialize(value: DslObject<Any>, jgen: JsonGenerator, provider: SerializerProvider) {
            if (value.value == null)
                return
            defaultSerializer.serialize(value, jgen, provider)
        }

        override fun isEmpty(provider: SerializerProvider?, value: DslObject<Any>?): Boolean {
            return value?.value == null
        }
    }
}

class StepDeserializer : StdDeserializer<Step>(Step::class.java) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Step {
        val node = p.readValueAsTree<TreeNode>()

        // Select the concrete class based on the existence of a property
        return when {
            node.get("get") != null -> p.codec.treeToValue(node, GenericGetStep::class.java)
            node.get("put") != null -> p.codec.treeToValue(node, GenericPutStep::class.java)
            node.get("task") != null -> p.codec.treeToValue(node, Step.TaskStep::class.java)
            node.get("do") != null -> p.codec.treeToValue(node, Step.DoStep::class.java)
            node.get("aggregate") != null -> p.codec.treeToValue(node, Step.AggregateStep::class.java)
            node.get("in_parallel") != null -> p.codec.treeToValue(node, Step.InParallelStep::class.java)
            node.get("try") != null -> p.codec.treeToValue(node, Step.TryStep::class.java)
            else -> {
                val fieldNames = mutableListOf<String>()
                node.fieldNames().forEach { fieldNames.add(it) }
                TODO("Cannot deserialize step, no standard step attributes found in ($fieldNames)")
            }
        }
    }
}

class ResourceDeserializer : StdDeserializer<GenericResource>(Resource::class.java) {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GenericResource {
        val node = p.readValueAsTree<TreeNode>()
        return p.codec.treeToValue(node, GenericResource::class.java)
    }
}

class TaskDslObjectDeserializer: DslObjectDeserializer<Task>(Task::class, { DslObject.from(::Task) })
class ResourceDslObjectDeserializer: DslObjectDeserializer<Task.Resource>(Task.Resource::class, { DslObject.from(Task::Resource) })
class RunConfigDslObjectDeserializer: DslObjectDeserializer<Task.RunConfig>(Task.RunConfig::class, { DslObject.from(Task::RunConfig) })
class ContainerLimitsDslObjectDeserializer : DslObjectDeserializer<Task.ContainerLimits>(Task.ContainerLimits::class, { DslObject.from(Task::ContainerLimits) })
class RetentionConfigDslObjectDeserializer : DslObjectDeserializer<Job.RetentionConfig>(Job.RetentionConfig::class, { DslObject.from(Job::RetentionConfig) })

open class DslObjectDeserializer<T : Any>(private val kClass: KClass<T>, private val ctor: () -> DslObject<T>) : JsonDeserializer<DslObject<T>>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): DslObject<T> {
        val node = parser.readValueAsTree<TreeNode>()
        val innerVal = parser.codec.treeToValue(node, kClass.java)
        val dslObj = ctor.invoke()
        dslObj.value = innerVal
        return dslObj
    }
}