package com.jacobibanez.godot.gpgs

import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.GamesSignInClient
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class GodotGooglePlayGameServices(godot: Godot) : GodotPlugin(godot) {

    private val tag: String = GodotGooglePlayGameServices::class.java.simpleName

    private lateinit var gamesSignInClient: GamesSignInClient
    private lateinit var achievementsClient: AchievementsClient
    private lateinit var leaderboardsClient: LeaderboardsClient

    override fun getPluginName(): String {
        return "GodotGooglePlayGameServices"
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(
            isUserAuthenticatedSuccess,
            isUserAuthenticatedFailure,
            requestServerSideAccessSuccess,
            requestServerSideAccessFailure,
            signInSuccess,
            signInFailure,
            onIncrementImmediateSuccess,
            onIncrementImmediateFailure
        )
    }

    @UsedByGodot
    fun initialize() {
        Log.d(tag, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
        setupClients()
        isAuthenticated()
    }

    @UsedByGodot
    fun isAuthenticated() {
        Log.d(tag, "Checking if user is authenticated")
        gamesSignInClient.isAuthenticated.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "User authenticated: ${task.result.isAuthenticated}")
                emitSignal(isUserAuthenticatedSuccess.name, task.result.isAuthenticated)
            } else {
                Log.e(tag, "User not authenticated. Cause: ${task.exception}", task.exception)
                emitSignal(isUserAuthenticatedFailure.name)
            }
        }
    }

    @UsedByGodot
    fun requestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) {
        Log.d(tag, "Requesting server side access for client id $serverClientId with refresh token $forceRefreshToken")
        gamesSignInClient.requestServerSideAccess(serverClientId, forceRefreshToken).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Access granted to server side for user: $serverClientId")
                emitSignal(requestServerSideAccessSuccess.name, task.result)
            } else {
                Log.e(tag, "Failed to request server side access. Cause: ${task.exception}", task.exception)
                emitSignal(requestServerSideAccessFailure.name)
            }
        }
    }

    @UsedByGodot
    fun signIn() {
        Log.d(tag, "Signing in")
        gamesSignInClient.signIn().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "User signed in: ${task.result.isAuthenticated}")
                emitSignal(signInSuccess.name, task.result.isAuthenticated)
            } else {
                Log.e(tag, "User not signed in. Cause: ${task.exception}", task.exception)
                emitSignal(signInFailure.name)
            }
        }
    }

    @UsedByGodot
    fun showAchievements() {
        Log.d(tag, "Showing achievements")
        achievementsClient.achievementsIntent.addOnSuccessListener { intent ->
            startActivityForResult(activity!!, intent, 9001, null)
        }
    }

    @UsedByGodot
    fun unlockAchievement(achievementId: String) {
        Log.d(tag, "Unlocking achievement with id $achievementId")
        achievementsClient.unlock(achievementId)
    }

    @UsedByGodot
    fun incrementAchievement(achievementId: String, amount: Int) {
        Log.d(tag, "Incrementing achievement with id $achievementId in an amount of $amount")
        achievementsClient.increment(achievementId, amount)
    }

    @UsedByGodot
    fun incrementImmediateAchievement(achievementId: String, amount: Int) {
        Log.d(tag, "Incrementing immediate achievement with id $achievementId in an amount of $amount")
        achievementsClient.incrementImmediate(achievementId, amount).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievement $achievementId incremented successfully. Unlocked? ${task.result}")
                emitSignal(onIncrementImmediateSuccess.name, task.result)
            } else {
                Log.e(tag, "Achievement $achievementId not incremented. Cause: ${task.exception}", task.exception)
                emitSignal(onIncrementImmediateFailure.name)
            }
        }
    }

    @UsedByGodot
    fun revealAchievement(achievementId: String) {
        Log.d(tag, "Revealing achievement with id $achievementId")
        achievementsClient.reveal(achievementId)
    }

    @UsedByGodot
    fun showAllLeaderboards() {
        Log.d(tag, "Showing all leaderboards")
        leaderboardsClient.allLeaderboardsIntent.addOnSuccessListener { intent ->
            startActivityForResult(activity!!, intent, 9002, null)
        }
    }

    private fun setupClients() {
        gamesSignInClient = PlayGames.getGamesSignInClient(activity!!)
        achievementsClient = PlayGames.getAchievementsClient(activity!!)
        leaderboardsClient = PlayGames.getLeaderboardsClient(activity!!)
    }
}