package com.jacobibanez.godot.gpgs.achievements

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.achievement.Achievement
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.fromAchievement
import com.jacobibanez.godot.gpgs.getAchievementFailure
import com.jacobibanez.godot.gpgs.getAchievementSuccess
import com.jacobibanez.godot.gpgs.incrementAchievementSuccess
import com.jacobibanez.godot.gpgs.incrementAchievementSuccessFailure
import com.jacobibanez.godot.gpgs.revealAchievementFailure
import com.jacobibanez.godot.gpgs.revealAchievementSuccess
import com.jacobibanez.godot.gpgs.unlockAchievementFailure
import com.jacobibanez.godot.gpgs.unlockAchievementSuccess
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

class AchievementsProxy(
    private val godot: Godot,
    private val achievementsClient: AchievementsClient = PlayGames.getAchievementsClient(godot.activity!!)
) {

    private val tag: String = AchievementsProxy::class.java.simpleName

    @SuppressLint("VisibleForTests")
    fun getAchievement(achievementId: String, forceReload: Boolean) {
        Log.d(tag, "Loading data for achievement: $achievementId")
        achievementsClient.load(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievements loaded successfully")
                Log.d(tag, "Achievements are stale? ${task.result.isStale}")
                Log.d(tag, "Number of achievements: ${task.result.get()?.count}")

                val achievements: List<Achievement> =
                    if (task.result.get() != null && task.result.get()!!.count > 0) {
                        task.result.get()!!.toList()
                    } else {
                        emptyList()
                    }

                val achievement = fromAchievement(achievements
                    .find { achievement -> achievement.achievementId == achievementId })
                emitSignal(godot, PLUGIN_NAME, getAchievementSuccess, achievement)
            } else {
                Log.e(tag, "Failed to load achievements. Cause: ${task.exception}", task.exception)
                emitSignal(godot, PLUGIN_NAME, getAchievementFailure)
            }
        }
    }

    fun incrementAchievement(achievementId: String, amount: Int) {
        Log.d(tag, "Incrementing achievement with id $achievementId in an amount of $amount")
        achievementsClient.incrementImmediate(achievementId, amount).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Achievement $achievementId incremented successfully. Unlocked? ${task.result}"
                )
                emitSignal(godot, PLUGIN_NAME, incrementAchievementSuccess, task.result)
            } else {
                Log.e(
                    tag,
                    "Achievement $achievementId not incremented. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(godot, PLUGIN_NAME, incrementAchievementSuccessFailure)
            }
        }
    }

    fun revealAchievement(achievementId: String) {
        Log.d(tag, "Revealing achievement with id $achievementId")
        achievementsClient.revealImmediate(achievementId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievement $achievementId revealed")
                emitSignal(godot, PLUGIN_NAME, revealAchievementSuccess)
            } else {
                Log.e(
                    tag,
                    "Achievement $achievementId not revealed. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(godot, PLUGIN_NAME, revealAchievementFailure)
            }
        }
    }

    fun showAchievements() {
        Log.d(tag, "Showing achievements")
        achievementsClient.achievementsIntent.addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(godot.activity!!, intent, 9001, null)
        }
    }

    fun unlockAchievement(achievementId: String) {
        Log.d(tag, "Unlocking achievement with id $achievementId")
        achievementsClient.unlockImmediate(achievementId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievement with id $achievementId unlocked")
                emitSignal(godot, PLUGIN_NAME, unlockAchievementSuccess)
            } else {
                Log.e(
                    tag,
                    "Error unlocking achievement $achievementId. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(godot, PLUGIN_NAME, unlockAchievementFailure)
            }
        }
    }
}