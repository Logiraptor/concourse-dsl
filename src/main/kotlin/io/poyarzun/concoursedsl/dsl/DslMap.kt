package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonUnwrapped

class DslMap<Key, Value>(@JsonUnwrapped val map: MutableMap<Key, Value>) : MutableMap<Key, Value> by map {
    companion object {
        fun <Key, Value> empty(): DslMap<Key, Value> {
            return DslMap(mutableMapOf())
        }
    }

    operator fun invoke(config: ConfigBlock<DslMap<Key, Value>>) = config(this)
}
