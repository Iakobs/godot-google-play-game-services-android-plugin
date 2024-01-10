# GDScript Reference

In this document is listed the plugin signals and methods available for use inside Godot with GDScript.
The source code is available [here](app/src/main/java/com/jacobibanez/godot/gpgs/GodotGooglePlayGameServices.kt).

After you install the plugin in `android/plugins` and enable it on your Export preset, you should be able to access its functionalities through:

```gdscript
Engine.get_singleton("GodotGooglePlayGameServices")
```

> ⚠️ Keep in mind that a wrapper with **full autocompletion** is already available at [this repository](https://github.com/Iakobs/godot-google-play-game-services-plugin), so prefer using it instead of interfacing with the native plugin singleton directly.

## Signals

These signals belong to the native plugin singleton and can be used by connecting them to a method.

```gdscript
func _ready() -> void:
    var plugin: JNISingleton = Engine.get_singleton("GodotGooglePlayGameServices")
    plugin.connect("leaderboardsAllLoaded", self, "_on_allLeaderboardsLoaded")
    plugin.loadAllLeaderboards(true)


func _on_allLeaderboardsLoaded(leaderboards: String) -> void:
    var parsed_leaderboards: Dictionary = JSON.parse(leaderboards).result
    prints(parsed_leaderboards)
```

### Achievements

#### achievementsLoaded(achievements: String)

This signal is emitted when calling the `achievementsLoad` method.
Returns A JSON string with a list of [Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement).

#### achievementsRevealed(revealed: bool, achievementId: String)

This signal is emitted when calling the `achievementsReveal` method.
Returns `true` if the achievement is revealed and `false` otherwise.
Also returns the id of the achievement.

#### achievementsUnlocked(isUnlocked: bool, achievementId: String)

This signal is emitted when calling the `achievementsIncrement`, `achievementsSetSteps` or `achievementsUnlock` methods.
Returns `true` if the achievement is unlocked or `false` otherwise.
Also returns the id of the achievement.

### Events

#### eventsLoaded(events: String)

This signal is emitted when calling the `eventsLoad` method.
Returns A JSON string with the list of [Event](https://developers.google.com/android/reference/com/google/android/gms/games/event/Event).

#### eventsLoadedByIds(events: String)

This signal is emitted when calling the `eventsLoadByIds` method.
Returns A JSON string with the list of [Event](https://developers.google.com/android/reference/com/google/android/gms/games/event/Event).

### Leaderboards

#### leaderboardsScoreSubmitted(submitted: bool, leaderboardId: String)

This signal is emitted when calling the `leaderboardsSubmitScore` method.
Returns `true` if the score is submitted. `false` otherwise. Also returns the id of the leaderboard.

#### leaderboardsScoreLoaded(leaderboardId: String, score: String)

This signal is emitted when calling the `leaderboardsLoadPlayerScore` method.
Return The leaderboard id and a JSON string with a [LeaderboardScore](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardScore).

#### leaderboardsAllLoaded(leaderboards: String)

This signal is emitted when calling the `leaderboardsLoadAll` method.
Returns A JSON string with a list of [Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).

#### leaderboardsLoaded(leaderboard: String)

This signal is emitted when calling the `leaderboardsLoad` method.
Returns A JSON string with a [Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).

### Players

#### playersCurrentLoaded(player: String)

This signal is emitted when calling the `playersLoadCurrent` method.
Returns A JSON string with a [Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).

#### playersFriendsLoaded(friends: String)

This signal is emitted when calling the `playersLoadFriends` method.
Returns A JSON with a list of [Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).

#### playersSearched(player: String)

This signal is emitted when selecting a player in the search window that is being displayed after calling the [playersSearch] method. Returns A JSON string with a [Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).

### Sign In

#### signInUserAuthenticated(isAuthenticated: bool)

This signal is emitted when calling the `signInIsAuthenticated` and `signInShowPopup` methods.
Returns `true` if the user is authenticated or `false` otherwise.

#### signInRequestedServerSideAccess(token: String)

This signal is emitted when calling the `signInRequestServerSideAccess` method.
Returns an OAuth 2.0 authorization token as a string.

### Snapshots

#### snapshotsGameSaved(saved: bool, fileName: String, description: String)

This signal is emitted when calling the `snapshotsSaveGame` method.
Returns a boolean indicating if the game was saved or not, and the name and description of the save file.

#### snapshotsGameLoaded(snapshot: String)

This signal is emitted when calling the `snapshotsLoadGame` method or after selecting a saved game in the window opened by the `snapshotsShowSavedGames` method.
Returns A JSON string representing a [Snapshot](https://developers.google.com/android/reference/com/google/android/gms/games/snapshot/Snapshot).

#### snapshotsConflictEmitted(conflict: String)

This signal is emitted when saving or loading a game, whenever a conflict occurs.
Returns A JSON string representing a [SnapshotConflict](https://developers.google.com/android/reference/com/google/android/gms/games/SnapshotsClient.SnapshotConflict).

## Methods

### Achievements

#### achievementsIncrement(achievementId: String, amount: int, immediate: bool)

Increments an achievement by the given number of steps.
The achievement must be an incremental achievement.
Once an achievement reaches at least the maximum number of steps, it will be unlocked automatically.
Any further increments will be ignored.
Emits `achievementsUnlocked`.

#### achievementsLoad(forceReload: bool)

Loads the achievement data for the currently signed-in player.
Emits `achievementsLoaded`.

#### achievementsReveal(achievementId: String, immediate: bool)

Reveals a hidden achievement to the currently signed-in player.
If the achievement has already been unlocked, this will have no effect.
Emits `achievementRevealed` if `immediate` is `true`.

#### achievementsSetSteps(achievementId: String, amount: int, immediate: bool)

Sets an achievement to have at least the given number of steps completed.
The achievement must be an incremental achievement.
Once an achievement reaches at least the maximum number of steps, it will be unlocked automatically.
Emits `achievementsUnlocked` if `immediate` is `true`.

#### achievementsShow()

Shows a native popup to browse game achievements of the currently signed-in player.

#### achievementsUnlock(achievementId: String, immediate: bool)

Unlocks an achievement for the currently signed in player.
If the achievement is hidden this will reveal it to the player.
Emits `achievementsUnlocked` if `immediate` is `true`.

### Events

#### eventsIncrement(eventId: String, amount: int)

Increments an event specified by eventId by the given number of steps.
This is the fire-and-forget form of the API (no signals are emitted).

#### eventsLoad(forceReload: bool)

Loads the event data for the currently signed-in player. Emits `eventsLoaded`.

#### eventsLoadByIds(forceReload: bool, eventIds: Array[String])

Loads the event data for the currently signed-in player for the specified ids. Emits `eventsLoadedByIds`.

### Leaderboards

#### leaderboardsShowAll()

Shows a native popup to browse all leaderboards.

#### leaderboardsShow(leaderboardId: String)

Shows a native popup to browse the specified leaderboard.

#### leaderboardsShowForTimeSpan(leaderboardId: String, timeSpan: Int)

Shows a native popup to browse the specified leaderboard for the specified time span.

#### leaderboardsShowForTimeSpanAndCollection(leaderboardId: String, timeSpan: int, collection: int)

Shows a native popup to browse the specified leaderboard for the specified time span and collection.

#### leaderboardsSubmitScore(leaderboardId: String, score: float)

Submits a score to the specified leaderboard. Emits `leaderboardsScoreSubmitted`.

#### leaderboardsLoadPlayerScore(leaderboardId: String, timeSpan: int, collection: int)

Loads the player's score for the specified leaderboard. Emits `leaderboardsScoreLoaded`.

#### leaderboardsLoadAll(forceReload: bool)

Loads the leaderboard data for the currently signed-in player. Emits `leaderboardsAllLoaded`.

#### leaderboardsLoad(leaderboardId: String, forceReload: bool)

Loads the leaderboard data for the currently signed-in player. Emits `leaderboardsLoaded`

### Players

#### playersCompareProfile(otherPlayerId: String)

Shows a native popup to compare two players.

#### playersCompareProfileWithAlternativeNameHints(otherPlayerId: String, otherPlayerInGameName: String, currentPlayerInGameName: String)

Shows a native popup to compare two players with alternative name hints.

#### playersLoadCurrent(forceReload: bool)

Loads the player data for the currently signed-in player. Emits `playersCurrentLoaded`.

#### playersLoadFriends(pageSize: int, forceReload: bool, askForPermission: bool)

Loads the friends data for the currently signed-in player. Emits `playersFriendsLoaded`.

#### playersSearch()

Shows a native popup to search for players. Emits `playersSearched`.

### Sign in

#### signInIsAuthenticated()

Checks if the user is signed in. Emits `signInUserAuthenticated`.

#### signInShowPopup()

Show a native popup to sign in the user. Emits `signInUserAuthenticated`.

#### signInRequestServerSideAccess(serverClientId: String, forceRefreshToken: bool)

Requests server-side access for the specified server client ID. Emits `signInRequestedServerSideAccess`.

### Snapshots

#### snapshotsLoadGame(fileName: String)

Loads a game from the specified file. Emits `snapshotsGameLoaded` or `snapshotsConflictEmitted`.

#### snapshotsSaveGame(fileName: String, description: String, saveData: PoolByteArray, playedTimeMillis: int, progressValue: int)

Saves a game to the specified file. Emits `snapshotsGameSaved` or `snapshotsConflictEmitted`.

#### snapshotsShowSavedGames(title: String, allowAddButton: bool, allowDelete: bool, maxSnapshots: int)

Shows a native popup to browse saved games. Emits `snapshotsGameLoaded`.
