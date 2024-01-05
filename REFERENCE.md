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
    plugin.connect("allLeaderboardsLoaded", self, "_on_allLeaderboardsLoaded")
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

This signal is emitted when calling the `loadEvents` method.
Returns A JSON string with the list of [Event](https://developers.google.com/android/reference/com/google/android/gms/games/event/Event).

#### eventsLoadedByIds(events: String)

This signal is emitted when calling the `loadEventsByIds` method.
Returns A JSON string with the list of [Event](https://developers.google.com/android/reference/com/google/android/gms/games/event/Event).

### Leaderboards

#### scoreSubmitted(submitted: bool, leaderboardId: String)

This signal is emitted when calling the `submitScore` method.
Returns `true` if the score is submitted. `false` otherwise. Also returns the id of the leaderboard.

#### scoreLoaded(leaderboardId: String, score: String)

This signal is emitted when calling the `loadPlayerScore` method.
Return The leaderboard id and a JSON string with a [LeaderboardScore](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardScore).

#### allLeaderboardsLoaded(leaderboards: String)

This signal is emitted when calling the `loadAllLeaderboards` method.
Returns A JSON string with a list of [Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).

#### leaderboardLoaded(leaderboard: String)

This signal is emitted when calling the `loadLeaderboard` method.
Returns A JSON string with a [Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard).

### Players

#### currentPlayerLoaded(player: String)

This signal is emitted when calling the `loadCurrentPlayer` method.
Returns A JSON string with a [Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).

#### friendsLoaded(friends: String)

This signal is emitted when calling the `loadFriends` method.
Returns A JSON with a list of [Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).

#### playerSearched(player: String)

This signal is emitted when selecting a player in the search window that is being displayed after calling the [searchPlayer] method. Returns A JSON string with a [Player](https://developers.google.com/android/reference/com/google/android/gms/games/Player).

### Sign In

#### userAuthenticated(isAuthenticated: bool)

This signal is emitted when calling the `isAuthenticated` and `signIn` methods.
Returns `true` if the user is authenticated or `false` otherwise.

#### requestedServerSideAccess(token: String)

This signal is emitted when calling the `requestServerSideAccess` method.
Returns an OAuth 2.0 authorization token as a string.

### Snapshots

#### gameSaved(saved: bool, fileName: String, description: String)

This signal is emitted when calling the `saveGame` method.
Returns a boolean indicating if the game was saved or not, and the name and description of the save file.

#### gameLoaded(snapshot: String)

This signal is emitted when calling the `loadGame` method or after selecting a saved game in the window opened by the `showSavedGames` method.
Returns A JSON string representing a [Snapshot](https://developers.google.com/android/reference/com/google/android/gms/games/snapshot/Snapshot).

#### conflictEmitted(conflict: String)

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

#### showAchievements()

Shows a native popup to browse game achievements of the currently signed-in player.

#### unlockAchievement(achievementId: String, immediate: bool)

Unlocks an achievement for the currently signed in player.
If the achievement is hidden this will reveal it to the player.
Emits `achievementsUnlocked` if `immediate` is `true`.

### Events

#### incrementEvent(eventId: String, amount: int)

Increments an event specified by eventId by the given number of steps.
This is the fire-and-forget form of the API (no signals are emitted).

#### loadEvents(forceReload: bool)

Loads the event data for the currently signed-in player. Emits `eventsLoaded`.

#### loadEventsByIds(forceReload: bool, eventIds: Array[String])

Loads the event data for the currently signed-in player for the specified ids. Emits `eventsLoadedByIds`.

### Leaderboards

#### showAllLeaderboards()

Shows a native popup to browse all leaderboards.

#### showLeaderboard(leaderboardId: String)

Shows a native popup to browse the specified leaderboard.

#### showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int)

Shows a native popup to browse the specified leaderboard for the specified time span.

#### showLeaderboardForTimeSpanAndCollection(leaderboardId: String, timeSpan: int, collection: int)

Shows a native popup to browse the specified leaderboard for the specified time span and collection.

#### submitScore(leaderboardId: String, score: float)

Submits a score to the specified leaderboard. Emits `scoreSubmitted`.

#### loadPlayerScore(leaderboardId: String, timeSpan: int, collection: int)

Loads the player's score for the specified leaderboard. Emits `scoreLoaded`.

#### loadAllLeaderboards(forceReload: bool)

Loads the leaderboard data for the currently signed-in player. Emits `allLeaderboardsLoaded`.

#### loadLeaderboard(leaderboardId: String, forceReload: bool)

Loads the leaderboard data for the currently signed-in player. Emits `leaderboardLoaded`

### Players

#### compareProfile(otherPlayerId: String)

Shows a native popup to compare two players.

#### compareProfileWithAlternativeNameHints(otherPlayerId: String, otherPlayerInGameName: String, currentPlayerInGameName: String)

Shows a native popup to compare two players with alternative name hints.

#### loadCurrentPlayer(forceReload: bool)

Loads the player data for the currently signed-in player. Emits `currentPlayerLoaded`.

#### loadFriends(pageSize: int, forceReload: bool, askForPermission: bool)

Loads the friends data for the currently signed-in player. Emits `friendsLoaded`.

#### searchPlayer()

Shows a native popup to search for players. Emits `playerSearched`.

### Sign in

#### isAuthenticated()

Checks if the user is signed in. Emits `userAuthenticated`.

#### signIn()

Signs in the user. Emits `userAuthenticated`.

#### requestServerSideAccess(serverClientId: String, forceRefreshToken: bool)

Requests server-side access for the specified server client ID. Emits `requestedServerSideAccess`.

### Snapshots

#### loadGame(fileName: String)

Loads a game from the specified file. Emits `gameLoaded` or `conflictEmitted`.

#### saveGame(fileName: String, description: String, saveData: PoolByteArray, playedTimeMillis: int, progressValue: int)

Saves a game to the specified file. Emits `gameSaved` or `conflictEmitted`.

#### showSavedGames(title: String, allowAddButton: bool, allowDelete: bool, maxSnapshots: int)

Shows a native popup to browse saved games. Emits `gameLoaded`.
