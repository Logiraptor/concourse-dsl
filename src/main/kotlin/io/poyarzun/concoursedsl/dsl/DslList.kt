package io.poyarzun.concoursedsl.dsl

import com.fasterxml.jackson.annotation.JsonUnwrapped
import io.poyarzun.concoursedsl.domain.ConcourseDslMarker

@ConcourseDslMarker
class DslList<T>(@JsonUnwrapped val list: MutableList<T> = mutableListOf()) : MutableList<T> by list {
    companion object {
        fun <T> empty(): DslList<T> = DslList()
    }

    operator fun invoke(config: ConfigBlock<DslList<T>>) = config(this)

    operator fun invoke(vararg elements: T) = addAll(elements)

    operator fun T.unaryPlus() = list.add(this)
}