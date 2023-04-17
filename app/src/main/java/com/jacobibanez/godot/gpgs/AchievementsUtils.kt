package com.jacobibanez.godot.gpgs

import com.google.android.gms.games.achievement.Achievement
import org.godotengine.godot.Dictionary

fun fromAchievement(achievement: Achievement?): Dictionary {
    return if (achievement != null) {
        Dictionary().apply {
            put("achievementId", achievement.achievementId)
            put("name", achievement.name)
            put("description", achievement.description)
            put("type", when (achievement.type) { 0 -> "STANDARD" else -> "INCREMENTAL" })
            put("state", when (achievement.state) { 0 -> "UNLOCKED" 1 -> "REVEALED" else -> "HIDDEN" })
            put("xpValue", achievement.xpValue)
            put("currentSteps", if (achievement.type == 1) achievement.currentSteps else 0)
            put("totalSteps", if (achievement.type == 1) achievement.totalSteps else 0)
            put("formattedCurrentSteps", if (achievement.type == 1) achievement.formattedCurrentSteps else 0)
            put("formattedTotalSteps", if (achievement.type == 1) achievement.formattedTotalSteps else 0)
            put("lastUpdatedTimestamp", achievement.lastUpdatedTimestamp)
        }
    } else {
        return Dictionary()
    }
}