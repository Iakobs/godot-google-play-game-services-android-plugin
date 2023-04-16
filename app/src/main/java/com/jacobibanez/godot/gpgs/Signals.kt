package com.jacobibanez.godot.gpgs

import org.godotengine.godot.plugin.SignalInfo

val isUserAuthenticatedSuccess = SignalInfo("isUserAuthenticatedSuccess", Boolean::class.javaObjectType)
val isUserAuthenticatedFailure = SignalInfo("isUserAuthenticatedFailure")
val requestServerSideAccessSuccess = SignalInfo("requestServerSideAccessSuccess", String::class.java)
val requestServerSideAccessFailure = SignalInfo("requestServerSideAccessFailure")
val signInSuccess = SignalInfo("signInSuccess", Boolean::class.javaObjectType)
val signInFailure = SignalInfo("signInFailure")

val onIncrementImmediateSuccess = SignalInfo("onIncrementImmediateSuccess", Boolean::class.javaObjectType)
val onIncrementImmediateFailure = SignalInfo("onIncrementImmediateFailure")