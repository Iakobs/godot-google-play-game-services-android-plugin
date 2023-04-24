package com.jacobibanez.godot.gpgs.friends

import android.util.Log
import com.google.android.gms.games.AnnotatedData
import com.google.android.gms.games.FriendsResolutionRequiredException
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayerBuffer
import com.google.android.gms.games.PlayersClient
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.loadFriendsFailure
import com.jacobibanez.godot.gpgs.loadFriendsSuccess
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

class FriendsProxy(
    private val godot: Godot,
    private val playersClient: PlayersClient = PlayGames.getPlayersClient(godot.activity!!)
) {

    private val tag: String = FriendsProxy::class.java.simpleName

    private val showSharingFriendsConsentRequestCode = 9006

    fun loadFriends(pageSize: Int, forceReload: Boolean) {
        Log.d(tag, "Loading friends with page size of $pageSize and forceReload = $forceReload")
        playersClient.loadFriends(pageSize, forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                emitListOfFriends(task)
            } else {
                if (task.exception is FriendsResolutionRequiredException) {
                    handleAskForFriendsListPermission(task)
                } else {
                    Log.e(
                        tag, "Unable to load friends. Cause: ${task.exception}", task.exception
                    )
                    emitSignal(godot, PLUGIN_NAME, loadFriendsFailure)
                }
            }
        }
    }

    private fun emitListOfFriends(task: Task<AnnotatedData<PlayerBuffer>>) {
        Log.d(
            tag,
            "Friends loaded. Friends are stale? ${task.result.isStale}"
        )
        val friendsCount = task.result.get()!!.count
        val friends: List<Dictionary> =
            if (task.result.get() != null && friendsCount > 0) {
                task.result.get()!!.map { fromPlayer(it) }.toList()
            } else {
                emptyList()
            }

        emitSignal(godot, PLUGIN_NAME, loadFriendsSuccess, Gson().toJson(friends))
    }

    private fun handleAskForFriendsListPermission(task: Task<AnnotatedData<PlayerBuffer>>) {
        val pendingIntent =
            (task.exception as FriendsResolutionRequiredException).resolution
        godot.activity!!.startIntentSenderForResult(
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