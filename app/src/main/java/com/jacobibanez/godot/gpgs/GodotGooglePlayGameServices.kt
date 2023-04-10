package com.jacobibanez.godot.gpgs

import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.games.AchievementsClient
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
    private lateinit var achievementsClient: AchievementsClient

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
        Log.d(tag, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
        setupClients()
        checkIsUserAuthenticated()
    }

    @UsedByGodot
    fun signIn() {
        signInClient.signIn()
        checkIsUserAuthenticated()
    }

    @UsedByGodot
    fun unlockAchievement(achievementId: String) {
        Log.d(tag, "Unlocking achievement with id $achievementId")
    }

    @UsedByGodot
    fun showAchievements() {
        achievementsClient.achievementsIntent.addOnSuccessListener { intent ->
            startActivityForResult(activity!!, intent, 9001, null)
        }
    }

    private fun setupClients() {
        signInClient = PlayGames.getGamesSignInClient(activity!!)
        achievementsClient = PlayGames.getAchievementsClient(activity!!)
    }

    private fun checkIsUserAuthenticated() {
        signInClient.isAuthenticated.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.isAuthenticated) {
                Log.d(tag, "User successfully authenticated. Result is ${task.result}")
                emitSignal(onUserAuthenticatedSuccess.name)
            } else {
                Log.d(tag, "User not authenticated. Exception is ${task.exception}")
                emitSignal(onUserAuthenticatedFailure.name)
            }
        }
    }
}