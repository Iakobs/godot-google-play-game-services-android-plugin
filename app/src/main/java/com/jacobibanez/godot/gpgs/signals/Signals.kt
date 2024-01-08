package com.jacobibanez.godot.gpgs.signals

import org.godotengine.godot.plugin.SignalInfo

/** @suppress */
fun getSignals(): MutableSet<SignalInfo> = mutableSetOf(
    AchievementsSignals.achievementsLoaded,
    AchievementsSignals.achievementsRevealed,
    AchievementsSignals.achievementsUnlocked,

    EventsSignals.eventsLoaded,
    EventsSignals.eventsLoadedByIds,

    LeaderboardSignals.leaderboardsScoreSubmitted,
    LeaderboardSignals.leaderboardsScoreLoaded,
    LeaderboardSignals.leaderboardsAllLoaded,
    LeaderboardSignals.leaderboardsLoaded,

    PlayerSignals.playersCurrentLoaded,
    PlayerSignals.playersFriendsLoaded,
    PlayerSignals.playersSearched,

    SignInSignals.userAuthenticated,
    SignInSignals.requestedServerSideAccess,

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
     * This signal is emitted when calling the
     * [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.achievementsIncrement],
     * [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.achievementsSetSteps] or
     * [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.achievementsUnlock] methods.
     *
     * @return `true` if the achievement is unlocked. `false` otherwise. Also returns the id of the achievement.
     */
    val achievementsUnlocked =
        SignalInfo("achievementsUnlocked", Boolean::class.javaObjectType, String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.achievementsLoad] method.
     *
     * @return A JSON with a list of [com.google.android.gms.games.achievement.Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement).
     */
    val achievementsLoaded =
        SignalInfo("achievementsLoaded", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.achievementsReveal] method.
     *
     * @return `true` if the achievement is revealed. `false` otherwise. Also returns the id of the achievement.
     */
    val achievementsRevealed =
        SignalInfo("achievementsRevealed", Boolean::class.javaObjectType, String::class.java)
}

/**
 * Signals emitted by Events methods.
 */
object EventsSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.eventsLoad] method.
     *
     * @return A JSON with the list of [com.google.android.gms.games.event.Event](https://developers.google.com/android/reference/com/google/android/gms/games/event/Event).
     */
    val eventsLoaded =
        SignalInfo("eventsLoaded", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.eventsLoadByIds] method.
     *
     * @return A JSON with the list of [com.google.android.gms.games.event.Event](https://developers.google.com/android/reference/com/google/android/gms/games/event/Event).
     */
    val eventsLoadedByIds =
        SignalInfo("eventsLoadedByIds", String::class.java)
}

/**
 * Signals emitted by Leaderboards methods.
 */
object LeaderboardSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.leaderboardsSubmitScore] method.
     *
     * @return `true` if the score is submitted. `false` otherwise. Also returns the id of the leaderboard.
     */
    val leaderboardsScoreSubmitted =
        SignalInfo("leaderboardsScoreSubmitted", Boolean::class.javaObjectType, String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.leaderboardsLoadPlayerScore] method.
     *
     * @return The leaderboard id and a JSON with a [com.google.android.gms.games.leaderboard.LeaderboardScore](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardScore).
     */
    val leaderboardsScoreLoaded = SignalInfo("leaderboardsScoreLoaded", String::class.java, String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.leaderboardsLoadAll] method.
     *
     * @return A JSON with a list of [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).
     */
    val leaderboardsAllLoaded = SignalInfo("leaderboardsAllLoaded", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.leaderboardsLoad] method.
     *
     * @return A JSON with a [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).
     */
    val leaderboardsLoaded = SignalInfo("leaderboardsLoaded", String::class.java)
}

/**
 * Signals emitted by Players methods.
 */
object PlayerSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.playersLoadFriends] method.
     *
     * @return A JSON with a list of [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).
     */
    val playersFriendsLoaded = SignalInfo("playersFriendsLoaded", String::class.java)

    /**
     * This signal is emitted when selecting a player in the search window that is being displayed after
     * calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.playersSearch] method.
     *
     * @return A JSON with a [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).
     */
    val playersSearched = SignalInfo("playersSearched", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.playersLoadCurrent] method.
     *
     * @return A JSON with a [com.google.android.gms.games.Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).
     */
    val playersCurrentLoaded = SignalInfo("playersCurrentLoaded", String::class.java)
}

/**
 * Signals emitted by Sign In methods.
 */
object SignInSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.requestServerSideAccess] method.
     *
     * @return An OAuth 2.0 authorization code as a string.
     */
    val requestedServerSideAccess = SignalInfo("requestedServerSideAccess", String::class.java)

    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.isAuthenticated]
     * and [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.signIn] methods.
     *
     * @return `true` if the user is authenticated. `false` otherwise.
     */
    val userAuthenticated = SignalInfo("userAuthenticated", Boolean::class.javaObjectType)
}

object SnapshotSignals {
    /**
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.saveGame] method.
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
     * This signal is emitted when calling the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.loadGame] method
     * or after selecting a saved game in the window opened by the [com.jacobibanez.godot.gpgs.GodotGooglePlayGameServices.showSavedGames] method.
     *
     * @return A JSON representing a [com.google.android.gms.games.snapshot.Snapshot](https://developers.google.com/android/reference/com/google/android/gms/games/snapshot/Snapshot).
     */
    val gameLoaded = SignalInfo("gameLoaded", String::class.java)

    /**
     * This signal is emitted when saving or loading a game, whenever a conflict occurs.
     *
     * @return A JSON representing a [com.google.android.gms.games.SnapshotsClient.SnapshotConflict](https://developers.google.com/android/reference/com/google/android/gms/games/SnapshotsClient.SnapshotConflict).
     */
    val conflictEmitted = SignalInfo("conflictEmitted", String::class.java)
}

object HelperSignals {
    /**
     * This signal is emitted everytime an image is downloaded and saved to the local storage.
     *
     * @return The stored file's path.
     */
    val imageStored = SignalInfo("imageStored", String::class.java)
}
