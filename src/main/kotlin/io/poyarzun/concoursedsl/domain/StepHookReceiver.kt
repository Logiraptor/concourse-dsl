package io.poyarzun.concoursedsl.domain

interface StepHookReceiver {
    var onSuccess: Step?
    var onFailure: Step?
    var onAbort: Step?
    var ensure: Step?
}