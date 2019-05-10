package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.reflect.KFunction4

class DslObject4<A, B, C, D, T : Any>(@JsonIgnore val function: KFunction4<A, B, C, D, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, b: B, c: C, d: D, config: ConfigBlock<T>) {
        value = function(a, b, c, d).apply(config)
    }
}