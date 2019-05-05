package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.DslList

data class Group(val name: String) {
    val jobs = DslList.empty<String>()
    val resources = DslList.empty<String>()
}