package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.reflect.KFunction1

class DslObject1<A, T : Any>(@JsonIgnore val function: KFunction1<A, T>) : DslObject<T>(null) {
    operator fun invoke(a: A, config: ConfigBlock<T>) {
        value = function(a).apply(config)
    }
}