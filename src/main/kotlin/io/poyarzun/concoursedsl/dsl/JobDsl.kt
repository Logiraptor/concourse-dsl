package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Task

fun Task.input(name: String, configBlock: ConfigBlock<Task.Input>) {
    inputs.add(Task.Input(name).apply(configBlock))
}

fun Task.output(name: String, configBlock: ConfigBlock<Task.Output>) {
    outputs.add(Task.Output(name).apply(configBlock))
}

fun Task.cache(name: String) {
    caches.add(Task.Cache(name))
}
