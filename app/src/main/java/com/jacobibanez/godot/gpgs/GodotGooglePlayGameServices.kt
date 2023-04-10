package com.jacobibanez.godot.gpgs

import android.util.Log
import com.google.android.gms.games.GamesSignInClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class GodotGooglePlayGameServices(godot: Godot) : GodotPlugin(godot) {

    private val tag: String = GodotGooglePlayGameServices::class.java.simpleName

    private lateinit var signInClient: GamesSignInClient

    override fun getPluginName(): String {
        return "GodotGooglePlayGameServices"
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(
            onUserAuthenticatedSuccess,
            onUserAuthenticatedFailure
        )
    }

    /**
     * Use this to test that the plugin works
     */
    @UsedByGodot
    fun helloWorld() {
        Log.i(tag, "Hello World")
    }

    @UsedByGodot
    fun initialize() {
        Log.i(tag, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
        signInClient = PlayGames.getGamesSignInClient(activity!!)

        checkIsUserAuthenticated() // You can comment this line to prevent your game to sign in on initializing
    }

    @UsedByGodot
    fun signIn() {
        signInClient.signIn()
        checkIsUserAuthenticated()
    }

    private fun checkIsUserAuthenticated() {
        signInClient.isAuthenticated.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.isAuthenticated) {
                Log.i(tag, "User successfully authenticated!")
                emitSignal(onUserAuthenticatedSuccess.name)
            } else {
                Log.i(tag, "User not authenticated :(")
                emitSignal(onUserAuthenticatedFailure.name)
            }
        }
    }
}