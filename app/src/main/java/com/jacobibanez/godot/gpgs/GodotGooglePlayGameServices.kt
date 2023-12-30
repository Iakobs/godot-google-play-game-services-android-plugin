package com.jacobibanez.godot.gpgs

import android.util.Log
import com.google.android.gms.games.PlayGamesSdk
import com.jacobibanez.godot.gpgs.achievements.AchievementsProxy
import com.jacobibanez.godot.gpgs.friends.FriendsProxy
import com.jacobibanez.godot.gpgs.leaderboards.LeaderboardsProxy
import com.jacobibanez.godot.gpgs.signin.SignInProxy
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

const val PLUGIN_NAME = "GodotGooglePlayGameServices"

class GodotGooglePlayGameServices(
    godot: Godot,
) : GodotPlugin(godot) {

    private val tag: String = GodotGooglePlayGameServices::class.java.simpleName
    private val achievementsProxy = AchievementsProxy(godot)
    private val friendsProxy = FriendsProxy(godot)
    private val signInProxy = SignInProxy(godot)
    private val leaderboardsProxy = LeaderboardsProxy(godot)

    override fun getPluginName(): String {
        return PLUGIN_NAME
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return getSignals()
    }

    @UsedByGodot
    fun initialize() {
        Log.d(tag, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
        isAuthenticated()
    }

    /* Achievements */
    @UsedByGodot
    fun incrementAchievement(achievementId: String, amount: Int) =
        achievementsProxy.incrementAchievement(achievementId, amount)

    @UsedByGodot
    fun loadAchievements(forceReload: Boolean) = achievementsProxy.loadAchievements(forceReload)

    @UsedByGodot
    fun revealAchievement(achievementId: String) =
        achievementsProxy.revealAchievement(achievementId)

    @UsedByGodot
    fun showAchievements() = achievementsProxy.showAchievements()

    @UsedByGodot
    fun unlockAchievement(achievementId: String) =
        achievementsProxy.unlockAchievement(achievementId)

    /* Friends */
    @UsedByGodot
    fun loadFriends(pageSize: Int, forceReload: Boolean) =
        friendsProxy.loadFriends(pageSize, forceReload)

    @UsedByGodot
    fun compareProfile(otherPlayerId: String) = friendsProxy.compareProfile(otherPlayerId)

    @UsedByGodot
    fun compareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) = friendsProxy.compareProfileWithAlternativeNameHints(
        otherPlayerId,
        otherPlayerInGameName,
        currentPlayerInGameName
    )

    /* Leaderboards */
    @UsedByGodot
    fun showAllLeaderboards() = leaderboardsProxy.showAllLeaderboards()

    @UsedByGodot
    fun showLeaderboard(leaderboardId: String) = leaderboardsProxy.showLeaderboard(leaderboardId)

    @UsedByGodot
    fun showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int) =
        leaderboardsProxy.showLeaderboardForTimeSpan(leaderboardId, timeSpan)

    @UsedByGodot
    fun showLeaderboardForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.showLeaderboardForTimeSpanAndCollection(
        leaderboardId, timeSpan, collection
    )

    @UsedByGodot
    fun submitScore(leaderboardId: String, score: Int) =
        leaderboardsProxy.submitScore(leaderboardId, score)

    /* SignIn */
    @UsedByGodot
    fun isAuthenticated() = signInProxy.isAuthenticated()

    @UsedByGodot
    fun requestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) =
        signInProxy.requestServerSideAccess(serverClientId, forceRefreshToken)

    @UsedByGodot
    fun signIn() = signInProxy.signIn()
}