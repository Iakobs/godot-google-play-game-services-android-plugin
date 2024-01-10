package com.jacobibanez.godot.gpgs.signin

import android.util.Log
import com.google.android.gms.games.GamesSignInClient
import com.google.android.gms.games.PlayGames
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.signals.SignInSignals.signInUserAuthenticated
import com.jacobibanez.godot.gpgs.signals.SignInSignals.signInRequestedServerSideAccess
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

class SignInProxy(
    private val godot: Godot,
    private val gamesSignInClient: GamesSignInClient = PlayGames.getGamesSignInClient(godot.getActivity()!!)
) {

    private val tag: String = SignInProxy::class.java.simpleName

    fun signInIsAuthenticated() {
        Log.d(tag, "Checking if user is authenticated")
        gamesSignInClient.isAuthenticated.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "User authenticated: ${task.result.isAuthenticated}")
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    signInUserAuthenticated,
                    task.result.isAuthenticated
                )
            } else {
                Log.e(tag, "User not authenticated. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    signInUserAuthenticated,
                    false
                )
            }
        }
    }

    fun signInRequestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) {
        Log.d(
            tag,
            "Requesting server side access for client id $serverClientId with refresh token $forceRefreshToken"
        )
        gamesSignInClient.requestServerSideAccess(serverClientId, forceRefreshToken)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "Access granted to server side for user: $serverClientId")
                    emitSignal(godot, PLUGIN_NAME, signInRequestedServerSideAccess, task.result)
                } else {
                    Log.e(
                        tag,
                        "Failed to request server side access. Cause: ${task.exception}",
                        task.exception
                    )
                    emitSignal(godot, PLUGIN_NAME, signInRequestedServerSideAccess)
                }
            }
    }

    fun signInShowPopup() {
        Log.d(tag, "Signing in")
        gamesSignInClient.signIn().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "User signed in: ${task.result.isAuthenticated}")
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    signInUserAuthenticated,
                    task.result.isAuthenticated
                )
            } else {
                Log.e(tag, "User not signed in. Cause: ${task.exception}", task.exception)
                emitSignal(godot, PLUGIN_NAME, signInUserAuthenticated, false)
            }
        }
    }
}
