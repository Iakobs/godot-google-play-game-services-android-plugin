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
        signInIsAuthenticated()
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
    fun eventsIncrement(eventId: String, amount: Int) =
        eventsProxy.increment(eventId, amount)

    @UsedByGodot
    fun eventsLoad(forceReload: Boolean) =
        eventsProxy.load(forceReload)

    @UsedByGodot
    fun eventsLoadByIds(forceReload: Boolean, eventIds: Array<String>) =
        eventsProxy.loadByIds(forceReload, eventIds)

    /* Leaderboards */
    @UsedByGodot
    fun leaderboardsShowAll() = leaderboardsProxy.showAll()

    @UsedByGodot
    fun leaderboardsShow(leaderboardId: String) = leaderboardsProxy.show(leaderboardId)

    @UsedByGodot
    fun leaderboardsShowForTimeSpan(leaderboardId: String, timeSpan: Int) =
        leaderboardsProxy.showForTimeSpan(leaderboardId, timeSpan)

    @UsedByGodot
    fun leaderboardsShowForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.showForTimeSpanAndCollection(
        leaderboardId, timeSpan, collection
    )

    @UsedByGodot
    fun leaderboardsSubmitScore(leaderboardId: String, score: Float) =
        leaderboardsProxy.submitScore(leaderboardId, score)

    @UsedByGodot
    fun leaderboardsLoadPlayerScore(leaderboardId: String, timeSpan: Int, collection: Int) =
        leaderboardsProxy.loadPlayerScore(leaderboardId, timeSpan, collection)

    @UsedByGodot
    fun leaderboardsLoadAll(forceReload: Boolean) =
        leaderboardsProxy.loadAll(forceReload)

    @UsedByGodot
    fun leaderboardsLoad(leaderboardId: String, forceReload: Boolean) =
        leaderboardsProxy.load(leaderboardId, forceReload)

    @UsedByGodot
    fun leaderboardsLoadPlayerCenteredScores(leaderboardId: String, timeSpan: Int, collection: Int, maxResults: Int, forceReload: Boolean) =
        leaderboardsProxy.loadPlayerCenteredScores(leaderboardId, timeSpan, collection, maxResults, forceReload)

    @UsedByGodot
    fun leaderboardsLoadTopScores(leaderboardId: String, timeSpan: Int, collection: Int, maxResults: Int, forceReload: Boolean) =
        leaderboardsProxy.loadTopScores(leaderboardId, timeSpan, collection, maxResults, forceReload)


    /* Players */
    @UsedByGodot
    fun playersCompareProfile(otherPlayerId: String) = playersProxy.playersCompareProfile(otherPlayerId)

    @UsedByGodot
    fun playersCompareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) = playersProxy.playersCompareProfileWithAlternativeNameHints(
        otherPlayerId,
        otherPlayerInGameName,
        currentPlayerInGameName
    )

    @UsedByGodot
    fun playersLoadCurrent(forceReload: Boolean) = playersProxy.playersLoadCurrent(forceReload)

    @UsedByGodot
    fun playersLoadFriends(pageSize: Int, forceReload: Boolean, askForPermission: Boolean) =
        playersProxy.playersLoadFriends(pageSize, forceReload, askForPermission)

    @UsedByGodot
    fun playersSearch() = playersProxy.playersSearch()

    /* SignIn */
    @UsedByGodot
    fun signInIsAuthenticated() = signInProxy.signInIsAuthenticated()

    @UsedByGodot
    fun signInRequestServerSideAccess(serverClientId: String, forceRefreshToken: Boolean) =
        signInProxy.signInRequestServerSideAccess(serverClientId, forceRefreshToken)

    @UsedByGodot
    fun signInShowPopup() = signInProxy.signInShowPopup()

    /* Snapshots */
    @UsedByGodot
    fun snapshotsLoadGame(fileName: String) = snapshotsProxy.loadGame(fileName)

    @UsedByGodot
    fun snapshotsSaveGame(
        fileName: String,
        description: String,
        saveData: ByteArray,
        playedTimeMillis: Int,
        progressValue: Int
    ) = snapshotsProxy.saveGame(fileName, description, saveData, playedTimeMillis.toLong(), progressValue.toLong())

    @UsedByGodot
    fun snapshotsShowSavedGames(
        title: String,
        allowAddButton: Boolean,
        allowDelete: Boolean,
        maxSnapshots: Int
    ) = snapshotsProxy.showSavedGames(title, allowAddButton, allowDelete, maxSnapshots)
}
