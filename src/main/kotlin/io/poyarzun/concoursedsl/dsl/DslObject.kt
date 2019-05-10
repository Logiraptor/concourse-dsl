package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonUnwrapped
import kotlin.reflect.*

open class DslObject<T : Any>(@get:JsonUnwrapped var value: T?) {
    companion object {
        fun <T : Any> from(function: KFunction<T>) = DslObject0(function)
        fun <A, T : Any> from(function: KFunction1<A, T>) = DslObject1(function)
        fun <A, B, T : Any> from(function: KFunction2<A, B, T>) = DslObject2(function)
        fun <A, B, C, T : Any> from(function: KFunction3<A, B, C, T>) = DslObject3(function)
        fun <A, B, C, D, T : Any> from(function: KFunction4<A, B, C, D, T>) = DslObject4(function)
    }
}