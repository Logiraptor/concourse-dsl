package io.poyarzun.concoursedsl.dsl

import io.poyarzun.concoursedsl.domain.Step

operator fun MutableList<Step>.invoke(init: Init<StepBuilder>) {
    StepBuilder(this::add).apply(init)
}
