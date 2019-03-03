package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonUnwrapped

class DslMap<Key, Value>(@JsonUnwrapped val map: MutableMap<Key, Value> = mutableMapOf()) : MutableMap<Key, Value> by map {
    companion object {
        fun <Key, Value> empty(): DslMap<Key, Value> {
            return DslMap()
        }
    }

    operator fun invoke(config: ConfigBlock<DslMap<Key, Value>>) = config(this)
}
