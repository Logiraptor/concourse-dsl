package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.poyarzun.concoursedsl.domain.Pipeline
import java.io.IOException

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

fun generateYML(pipeline: Pipeline): String {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule())
    mapper.registerModule(DslModule)
    return mapper.writeValueAsString(pipeline)
}
