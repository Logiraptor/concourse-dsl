package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlin.reflect.*

class TopLevel {
    var run = DslObject.from(::RunConfig)
    var run2 = DslObject.from(::RunConfig2)
}

class RunConfig {
    var path: String? = null
    val args: DslList<String> = DslList.empty()
    var dir: String? = null
    var user: String? = null

    val params: DslMap<String, String> = DslMap.empty()
}

class RunConfig2(val path: String) {
    val args: DslList<String> = DslList.empty()
    var dir: String? = null
    var user: String? = null
}

class DslMap<Key, Value>(@JsonUnwrapped val map: MutableMap<Key, Value>) : MutableMap<Key, Value> by map {
    companion object {
        fun <Key, Value> empty(): DslMap<Key, Value> {
            return DslMap(mutableMapOf())
        }
    }

    operator fun invoke(config: ConfigBlock<DslMap<Key, Value>>) = config(this)
}

open class DslObject<T : Any>(value: T?) {

    @JsonIgnore
    var lastValue: T? = value

    var value: T
        @JsonUnwrapped
        get() = lastValue as T
        set(nextValue) {
            lastValue = nextValue
        }

    companion object {
        fun <T : Any> from(function: KFunction<T>) = DslObject0(function)
        fun <A, T : Any> from(function: KFunction1<A, T>) = DslObject1(function)
        fun <A, B, T : Any> from(function: KFunction2<A, B, T>) = DslObject2(function)
        fun <A, B, C, T : Any> from(function: KFunction3<A, B, C, T>) = DslObject3(function)
        fun <A, B, C, D, T : Any> from(function: KFunction4<A, B, C, D, T>) = DslObject4(function)
    }
}

class DslObject0<T : Any>(@JsonIgnore val function: KFunction<T>) : DslObject<T>(null) {
    operator fun invoke(config: ConfigBlock<T>) {
        value = function.call().apply(config)
    }
}

class DslObject1<A, T : Any>(@JsonIgnore val function: KFunction1<A, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, config: ConfigBlock<T>) {
        value = function(a).apply(config)
    }
}

class DslObject2<A, B, T : Any>(@JsonIgnore val function: KFunction2<A, B, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, b: B, config: ConfigBlock<T>) {
        value = function(a, b).apply(config)
    }
}

class DslObject3<A, B, C, T : Any>(@JsonIgnore val function: KFunction3<A, B, C, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, b: B, c: C, config: ConfigBlock<T>) {
        value = function(a, b, c).apply(config)
    }
}

class DslObject4<A, B, C, D, T : Any>(@JsonIgnore val function: KFunction4<A, B, C, D, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, b: B, c: C, d: D, config: ConfigBlock<T>) {
        value = function(a, b, c, d).apply(config)
    }
}

class DslList<T>(@JsonUnwrapped val list: MutableList<T>) : MutableList<T> by list {
    companion object {
        fun <T> empty(): DslList<T> = DslList(mutableListOf())
    }

    operator fun invoke(config: ConfigBlock<DslList<T>>) = config(this)

    operator fun invoke(vararg elements: T) = elements.forEach { +it }

    operator fun T.unaryPlus() = list.add(this)
}

fun main() {
    val exp = TopLevel().apply {
        run {
            path = "foo"
            args("Foo")

            params {
                this["repo"] = ""
            }
        }

        run2("path") {
            args {
                +"ABC"
            }

            dir = "omg lol"
        }
    }

    val mapper = ObjectMapper(YAMLFactory())
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule())
    val output = mapper.writeValueAsString(exp)

    println(output)
}
