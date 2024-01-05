package com.jacobibanez.godot.gpgs

import android.util.Log
import com.google.android.gms.games.PlayGamesSdk
import com.jacobibanez.godot.gpgs.achievements.AchievementsProxy
import com.jacobibanez.godot.gpgs.events.EventsProxy
import com.jacobibanez.godot.gpgs.leaderboards.LeaderboardsProxy
import com.jacobibanez.godot.gpgs.signin.SignInProxy
import com.jacobibanez.godot.gpgs.players.PlayersProxy
import com.jacobibanez.godot.gpgs.snapshots.SnapshotsProxy
import com.jacobibanez.godot.gpgs.signals.getSignals
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
    private val eventsProxy = EventsProxy(godot)
    private val signInProxy = SignInProxy(godot)
    private val leaderboardsProxy = LeaderboardsProxy(godot)
    private val playersProxy = PlayersProxy(godot)
    private val snapshotsProxy = SnapshotsProxy(godot)

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
    fun achievementsIncrement(achievementId: String, amount: Int, immediate: Boolean) =
        achievementsProxy.increment(achievementId, amount, immediate)

    @UsedByGodot
    fun achievementsLoad(forceReload: Boolean) = achievementsProxy.load(forceReload)

    @UsedByGodot
    fun achievementsReveal(achievementId: String, immediate: Boolean) =
        achievementsProxy.reveal(achievementId, immediate)

    @UsedByGodot
    fun achievementsSetSteps(achievementId: String, amount: Int, immediate: Boolean) =
        achievementsProxy.setSteps(achievementId, amount, immediate)

    @UsedByGodot
    fun achievementsShow() = achievementsProxy.show()

    @UsedByGodot
    fun achievementsUnlock(achievementId: String, immediate: Boolean) =
        achievementsProxy.unlock(achievementId, immediate)

    /* Events */
    @UsedByGodot
    fun incrementEvent(eventId: String, amount: Int) =
        eventsProxy.increment(eventId, amount)

    @UsedByGodot
    fun loadEvents(forceReload: Boolean) =
        eventsProxy.load(forceReload)

    @UsedByGodot
    fun loadEventsByIds(forceReload: Boolean, eventIds: Array<String>) =
        eventsProxy.loadByIds(forceReload, eventIds)

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
    fun submitScore(leaderboardId: String, score: Float) =
        leaderboardsProxy.submitScore(leaderboardId, score)

    @UsedByGodot
    fun loadPlayerScore(leaderboardId: String, timeSpan: Int, collection: Int) =
        leaderboardsProxy.loadPlayerScore(leaderboardId, timeSpan, collection)

    @UsedByGodot
    fun loadAllLeaderboards(forceReload: Boolean) =
        leaderboardsProxy.loadAllLeaderboards(forceReload)

    @UsedByGodot
    fun loadLeaderboard(leaderboardId: String, forceReload: Boolean) =
        leaderboardsProxy.loadLeaderboard(leaderboardId, forceReload)

    /* Players */
    @UsedByGodot
    fun compareProfile(otherPlayerId: String) = playersProxy.compareProfile(otherPlayerId)

    @UsedByGodot
    fun compareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) = playersProxy.compareProfileWithAlternativeNameHints(
        otherPlayerId,
        otherPlayerInGameName,
        currentPlayerInGameName
    )

    @UsedByGodot
    fun loadCurrentPlayer(forceReload: Boolean) = playersProxy.loadCurrentPlayer(forceReload)

    @UsedByGodot
    fun loadFriends(pageSize: Int, forceReload: Boolean, askForPermission: Boolean) =
        playersProxy.loadFriends(pageSize, forceReload, askForPermission)

    @UsedByGodot
    fun searchPlayer() = playersProxy.searchPlayer()

    /* SignIn */
    @UsedByGodot
    fun isAuthenticated() = signInProxy.isAuthenticated()

    @UsedByGodot
    fun requestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) =
        signInProxy.requestServerSideAccess(serverClientId, forceRefreshToken)

    @UsedByGodot
    fun signIn() = signInProxy.signIn()

    /* Snapshots */
    @UsedByGodot
    fun loadGame(fileName: String) = snapshotsProxy.loadGame(fileName)

    @UsedByGodot
    fun saveGame(
        fileName: String,
        description: String,
        saveData: ByteArray,
        playedTimeMillis: Int,
        progressValue: Int
    ) = snapshotsProxy.saveGame(fileName, description, saveData, playedTimeMillis.toLong(), progressValue.toLong())

    @UsedByGodot
    fun showSavedGames(
        title: String,
        allowAddButton: Boolean,
        allowDelete: Boolean,
        maxSnapshots: Int
    ) = snapshotsProxy.showSavedGames(title, allowAddButton, allowDelete, maxSnapshots)
}
