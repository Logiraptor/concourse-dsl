package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.reflect.KFunction3

class DslObject3<A, B, C, T : Any>(@JsonIgnore val function: KFunction3<A, B, C, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, b: B, c: C, config: ConfigBlock<T>) {
        value = function(a, b, c).apply(config)
    }
}