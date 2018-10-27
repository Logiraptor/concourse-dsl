package io.poyarzun.concoursedsl.domain

data class Group(
    val name: String,
    val jobs: MutableList<String> = ArrayList(),
    val resources: MutableList<String> = ArrayList()
)