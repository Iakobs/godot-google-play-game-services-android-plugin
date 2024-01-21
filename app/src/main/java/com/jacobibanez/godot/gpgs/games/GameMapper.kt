package com.jacobibanez.godot.gpgs.games

import com.google.android.gms.games.Game
import com.jacobibanez.godot.gpgs.utils.toStringAndSave
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot

fun fromGame(godot: Godot, game: Game) = Dictionary().apply {
    put("areSnapshotsEnabled", game.areSnapshotsEnabled())
    put("achievementTotalCount", game.achievementTotalCount)
    put("applicationId", game.applicationId)
    put("description", game.description)
    put("developerName", game.developerName)
    put("displayName", game.displayName)
    put("leaderboardCount", game.leaderboardCount)
    game.primaryCategory.let { put("primaryCategory", it) }
    game.secondaryCategory.let { put("secondaryCategory", it) }
    put("themeColor", game.themeColor)
    put("hasGamepadSupport", game.hasGamepadSupport())
    put(
        "hiResImageUri",
        game.hiResImageUri?.toStringAndSave(godot, "hiResImageUri", game.applicationId) ?: ""
    )
    put(
        "iconImageUri",
        game.iconImageUri?.toStringAndSave(godot, "iconImageUri", game.applicationId) ?: ""
    )
    put(
        "featuredImageUri",
        game.featuredImageUri?.toStringAndSave(godot, "featuredImageUri", game.applicationId) ?: ""
    )
}
