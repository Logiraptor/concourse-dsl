package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
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