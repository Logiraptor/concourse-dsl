package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.ConfigBlock
import io.poyarzun.concoursedsl.dsl.DslList

data class Group(val name: String) {
    val jobs = DslList.empty<String>()
    val resources = DslList.empty<String>()
}

fun group(name: String, configBlock: ConfigBlock<Group>) =
        Group(name).apply(configBlock)
