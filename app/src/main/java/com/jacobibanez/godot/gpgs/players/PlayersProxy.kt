package com.jacobibanez.godot.gpgs.players

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.FriendsResolutionRequiredException
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.Player
import com.google.android.gms.games.PlayersClient
import com.google.android.gms.games.PlayersClient.EXTRA_PLAYER_SEARCH_RESULTS
import com.google.gson.Gson
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.signals.PlayerSignals.playersCurrentLoaded
import com.jacobibanez.godot.gpgs.signals.PlayerSignals.playersFriendsLoaded
import com.jacobibanez.godot.gpgs.signals.PlayerSignals.playersSearched
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

/** @suppress */
class PlayersProxy(
    private val godot: Godot,
    private val playersClient: PlayersClient = PlayGames.getPlayersClient(godot.getActivity()!!)
) {

    private val tag: String = PlayersProxy::class.java.simpleName

    private val showSharingFriendsConsentRequestCode = 9006
    private val compareProfileRequestCode = 9007
    private val compareProfileWithAlternativeNameHintsRequestCode = 9008
    private val searchPlayerCode = 9009

    /** @suppress */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == searchPlayerCode && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                try {
                    val players = intent
                        .extras?.get(EXTRA_PLAYER_SEARCH_RESULTS) as ArrayList<Player>
                    Log.d(tag, "Players selected from search: $players")
                    emitSignal(
                        godot,
                        PLUGIN_NAME,
                        playersSearched,
                        Gson().toJson(fromPlayer(godot, players.first()))
                    )
                } catch (exception: Exception) {
                    Log.e(
                        tag,
                        "Error while receiving player from search: ${exception.localizedMessage}"
                    )
                }
            }
        }
    }

    fun playersLoadFriends(pageSize: Int, forceReload: Boolean, askForPermission: Boolean) {
        Log.d(tag, "Loading friends with page size of $pageSize and forceReload = $forceReload")
        playersClient.loadFriends(pageSize, forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Friends loaded. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer = task.result.get()!!
                val friendsCount = safeBuffer.count
                val friends: List<Dictionary> =
                    if (friendsCount > 0) {
                        safeBuffer.map { fromPlayer(godot, it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    playersFriendsLoaded,
                    Gson().toJson(friends)
                )
            } else {
                if (task.exception is FriendsResolutionRequiredException && askForPermission) {
                    askForFriendsListPermission(task.exception as FriendsResolutionRequiredException)
                } else {
                    Log.e(
                        tag, "Unable to load friends. Cause: ${task.exception}", task.exception
                    )
                    emitSignal(
                        godot,
                        PLUGIN_NAME,
                        playersFriendsLoaded,
                        Gson().toJson(emptyList<Dictionary>())
                    )
                }
            }
        }
    }

    fun playersCompareProfile(otherPlayerId: String) {
        Log.d(tag, "Comparing profile with player with id $otherPlayerId")
        playersClient.getCompareProfileIntent(otherPlayerId).addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                compareProfileRequestCode,
                null
            )
        }
    }

    fun playersCompareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) {
        Log.d(
            tag, "Comparing profile with player with id $otherPlayerId. " +
                    "Current player in-game name: $currentPlayerInGameName. " +
                    "Other player in-game name: $otherPlayerInGameName"
        )
        playersClient.getCompareProfileIntentWithAlternativeNameHints(
            otherPlayerId,
            otherPlayerInGameName,
            currentPlayerInGameName
        ).addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                compareProfileWithAlternativeNameHintsRequestCode,
                null
            )
        }
    }

    fun playersSearch() {
        Log.d(tag, "Opening search intent.")
        playersClient.playerSearchIntent.addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!,
                intent,
                searchPlayerCode,
                null
            )
        }
    }

    fun playersLoadCurrent(forceReload: Boolean) {
        Log.d(tag, "Loading current player. Force reload? $forceReload")
        playersClient.getCurrentPlayer(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Current player loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val player: Dictionary? = task.result.get()?.let { fromPlayer(godot, it) }
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    playersCurrentLoaded,
                    Gson().toJson(player)
                )
            } else {
                Log.e(
                    tag,
                    "Failed to load current player. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    playersCurrentLoaded,
                    Gson().toJson(null)
                )
            }
        }
    }

    private fun askForFriendsListPermission(exception: FriendsResolutionRequiredException) {
        val pendingIntent = exception.resolution
        godot.getActivity()!!.startIntentSenderForResult(
            pendingIntent.intentSender,
            showSharingFriendsConsentRequestCode,
            null,
            0,
            0,
            0,
            null
        )
    }
}