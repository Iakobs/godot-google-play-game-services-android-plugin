package com.jacobibanez.godot.gpgs.events

import android.util.Log
import com.google.gson.Gson
import com.google.android.gms.games.EventsClient
import com.google.android.gms.games.event.EventBuffer
import com.google.android.gms.games.PlayGames
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.signals.EventsSignals.eventsLoaded
import com.jacobibanez.godot.gpgs.signals.EventsSignals.eventsLoadedByIds
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal
import org.godotengine.godot.Dictionary
import com.jacobibanez.godot.gpgs.events.fromEvent

class EventsProxy(
    private val godot: Godot,
    private val eventsClient: EventsClient = PlayGames.getEventsClient(godot.getActivity()!!)
) {

    private val tag: String = EventsProxy::class.java.simpleName

    fun increment(eventId: String, incrementAmount: Int) {
        Log.d(tag, "Submitting event $eventId by value $incrementAmount")
        eventsClient.increment(eventId, incrementAmount)
    }

    fun load(forceReload: Boolean) {
        Log.d(tag, "Retrieving events for this user (forceReload = $forceReload)")
        eventsClient.load(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Events loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer: EventBuffer = task.result.get()!!
                val eventsCount = safeBuffer.count
                val events: List<Dictionary> =
                    if (eventsCount > 0) {
                        safeBuffer.map { fromEvent(godot, it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    eventsLoaded,
                    Gson().toJson(events)
                )
            } else {
                Log.e(tag, "Failed to load events. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    eventsLoaded,
                    Gson().toJson(emptyList<Dictionary>())
                )
            }
        }
    }

    fun loadByIds(forceReload: Boolean, eventIds: List<String>) {
        Log.d(tag, "Retrieving events for from eventIds $eventIds (forceReload = $forceReload)")
        eventsClient.loadByIds(forceReload, *(eventIds.toTypedArray())).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Events loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer: EventBuffer = task.result.get()!!
                val eventsCount = safeBuffer.count
                val events: List<Dictionary> =
                    if (eventsCount > 0) {
                        safeBuffer.map { fromEvent(godot, it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    eventsLoadedByIds,
                    Gson().toJson(events)
                )
            } else {
                Log.e(tag, "Failed to load events. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    eventsLoadedByIds,
                    Gson().toJson(emptyList<Dictionary>())
                )
            }
        }
    }
}
