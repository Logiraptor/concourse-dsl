package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.reflect.KFunction2

class DslObject2<A, B, T : Any>(@JsonIgnore val function: KFunction2<A, B, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, b: B, config: ConfigBlock<T>) {
        value = function(a, b).apply(config)
    }
}