package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonUnwrapped

class DslList<T>(@JsonUnwrapped val list: MutableList<T>) : MutableList<T> by list {
    companion object {
        fun <T> empty(): DslList<T> = DslList(mutableListOf())
    }

    operator fun invoke(config: ConfigBlock<DslList<T>>) = config(this)

    operator fun invoke(vararg elements: T) = elements.forEach { +it }

    operator fun T.unaryPlus() = list.add(this)
}