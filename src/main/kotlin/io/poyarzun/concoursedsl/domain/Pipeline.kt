package io.poyarzun.concoursedsl.domain

data class Pipeline(
    var jobs: MutableList<Job> = ArrayList(),
    var groups: MutableList<Group> = ArrayList(),
    var resources: MutableList<Resource> = ArrayList(),
    var resourceTypes: MutableList<ResourceType> = ArrayList()
)

