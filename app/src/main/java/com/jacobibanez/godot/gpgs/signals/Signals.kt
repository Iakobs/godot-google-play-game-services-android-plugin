package com.jacobibanez.godot.gpgs.signals

import org.godotengine.godot.Dictionary
import org.godotengine.godot.plugin.SignalInfo

/** @suppress */
fun getSignals(): MutableSet<SignalInfo> = mutableSetOf(
    SignInSignals.userAuthenticated,
    SignInSignals.requestedServerSideAccess,

    AchievementsSignals.achievementUnlocked,
    AchievementsSignals.achievementsLoaded,
    AchievementsSignals.achievementRevealed,

    EventsSignals.eventsLoaded,
    EventsSignals.eventsLoadedByIds,

    LeaderboardSignals.scoreSubmitted,
    LeaderboardSignals.scoreLoaded,
    LeaderboardSignals.allLeaderboardsLoaded,
    LeaderboardSignals.leaderboardLoaded,

    PlayerSignals.friendsLoaded,
    PlayerSignals.playerSearched,
    PlayerSignals.currentPlayerLoaded,

    SnapshotSignals.gameSaved,
    SnapshotSignals.gameLoaded,
    SnapshotSignals.conflictEmitted,

    HelperSignals.imageStored,
)

/**
 * Signals emitted by Achievements methods.
 */
object AchievementsSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.incrementAchievement]
     * or [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.unlockAchievement] methods.
     *
     * @return `true` if the achievement is unlocked. `false` otherwise. Also returns the id of the achievement.
     */
    val achievementUnlocked =
        SignalInfo("achievementUnlocked", Boolean::class.javaObjectType, String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadAchievements] method.
     *
     * @return A JSON with a list of [com.google.android.gms.games.achievement.Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement).
     */
    val achievementsLoaded = SignalInfo("achievementsLoaded", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.revealAchievement] method.
     *
     * @return `true` if the achievement is revealed. `false` otherwise. Also returns the id of the achievement.
     */
    val achievementRevealed =
        SignalInfo("achievementRevealed", Boolean::class.javaObjectType, String::class.java)
}

/**
 * Signals emitted by Events methods.
 */
object EventsSignals {
    val eventsLoaded =
        SignalInfo("eventsLoaded", String::class.java)

    val eventsLoadedByIds =
        SignalInfo("eventsLoadedByIds", String::class.java)
}

/**
 * Signals emitted by Leaderboards methods.
 */
object LeaderboardSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.submitScore] method.
     *
     * @return `true` if the score is submitted. `false` otherwise. Also returns the id of the leaderboard.
     */
    val scoreSubmitted =
        SignalInfo("scoreSubmitted", Boolean::class.javaObjectType, String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadPlayerScore] method.
     *
     * @return The leaderboard id and a JSON with a [com.google.android.gms.games.leaderboard.LeaderboardScore](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardScore).
     */
    val scoreLoaded = SignalInfo("scoreLoaded", String::class.java, String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadAllLeaderboards] method.
     *
     * @return A JSON with a list of [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).
     */
    val allLeaderboardsLoaded = SignalInfo("allLeaderboardsLoaded", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadLeaderboard] method.
     *
     * @return A JSON with a [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).
     */
    val leaderboardLoaded = SignalInfo("leaderboardLoaded", String::class.java)
}

/**
 * Signals emitted by Players methods.
 */
object PlayerSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadFriends] method.
     *
     * @return A JSON with a list of [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).
     */
    val friendsLoaded = SignalInfo("friendsLoaded", String::class.java)

    /**
     * This signal is emitted when selecting a player in the search window that is being displayed after
     * calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.searchPlayer] method.
     *
     * @return A JSON with a [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).
     */
    val playerSearched = SignalInfo("playerSearched", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadCurrentPlayer] method.
     *
     * @return A JSON with a [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).
     */
    val currentPlayerLoaded = SignalInfo("currentPlayerLoaded", String::class.java)
}

/**
 * Signals emitted by Sign In methods.
 */
object SignInSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.requestServerSideAccess] method.
     *
     * @return An OAuth 2.0 authorization code as a string.
     */
    val requestedServerSideAccess = SignalInfo("requestedServerSideAccess", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.isAuthenticated]
     * and [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.signIn] methods.
     *
     * @return `true` if the user is authenticated. `false` otherwise.
     */
    val userAuthenticated = SignalInfo("userAuthenticated", Boolean::class.javaObjectType)
}

object SnapshotSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.saveGame] method.
     *
     * @return A boolean indicating if the game was saved or not, and the name and description of the save file.
     */
    val gameSaved = SignalInfo(
        "gameSaved",
        Boolean::class.javaObjectType,
        String::class.java,
        String::class.java
    )

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.loadGame] method
     * or after selecting a saved game in the window opened by the [com.jacobibanez.godot.gpgs.GodotAndroidPlugin.showSavedGames] method.
     *
     * @return A [Dictionary] representing a [com.google.android.gms.games.snapshot.Snapshot](https://developers.google.com/android/reference/com/google/android/gms/games/snapshot/Snapshot).
     */
    val gameLoaded = SignalInfo("gameLoaded", Dictionary::class.java)

    /**
     * This signal is emitted when saving or loading a game, whenever a conflict occurs.
     *
     * @return A [Dictionary] representing a [com.google.android.gms.games.SnapshotsClient.SnapshotConflict](https://developers.google.com/android/reference/com/google/android/gms/games/SnapshotsClient.SnapshotConflict).
     */
    val conflictEmitted = SignalInfo("conflictEmitted", Dictionary::class.java)
}

object HelperSignals {
    /**
     * This signal is emitted everytime an image is downloaded and saved to the local storage.
     *
     * @return The stored file's path.
     */
    val imageStored = SignalInfo("imageStored", String::class.java)
}
