package com.jacobibanez.godot.gpgs

import org.godotengine.godot.plugin.SignalInfo

val onUserAuthenticatedSuccess = SignalInfo("onUserAuthenticatedSuccess")
val onUserAuthenticatedFailure = SignalInfo("onUserAuthenticatedFailure")

val onIncrementImmediateSuccess = SignalInfo("onIncrementImmediateSuccess", Boolean::class.javaObjectType)
val onIncrementImmediateFailure = SignalInfo("onIncrementImmediateFailure")