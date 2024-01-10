package com.jacobibanez.godot.gpgs.events

import com.google.android.gms.games.event.Event
import com.jacobibanez.godot.gpgs.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import com.jacobibanez.godot.gpgs.players.fromPlayer

fun fromEvent(godot: Godot, event: Event) = Dictionary().apply {
    put("description", event.description)
    put("eventId", event.eventId)
    put("formattedValue", event.formattedValue)
    event.iconImageUri.let {
        put(
            "iconImageUri",
            it.toStringAndSave(godot, "iconImageUri", event.eventId)
        )
    }
    put("name", event.name)
    put("player", fromPlayer(godot, event.player))
    put("value", event.value)
    put("isVisible", event.isVisible)
}
