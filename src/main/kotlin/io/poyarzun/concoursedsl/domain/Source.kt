package io.poyarzun.concoursedsl.domain

import io.poyarzun.concoursedsl.dsl.DslList
import io.poyarzun.concoursedsl.dsl.DslMap

typealias Source = DslMap<String, Any?>
typealias Params = DslMap<String, Any?>
typealias Version = DslMap<String, String>

typealias Tags = DslList<String>
