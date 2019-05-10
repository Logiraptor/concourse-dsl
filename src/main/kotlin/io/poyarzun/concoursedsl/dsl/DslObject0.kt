package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.reflect.KFunction

class DslObject0<T : Any>(@JsonIgnore val function: KFunction<T>) : DslObject<T>(null) {
    operator fun invoke(config: ConfigBlock<T>) {
        value = function.call().apply(config)
    }
}