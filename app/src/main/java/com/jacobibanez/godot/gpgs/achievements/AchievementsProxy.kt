package com.jacobibanez.godot.gpgs.achievements

import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.PlayGames
import com.google.gson.Gson
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.incrementAchievementSuccess
import com.jacobibanez.godot.gpgs.incrementAchievementSuccessFailure
import com.jacobibanez.godot.gpgs.loadAchievementsFailure
import com.jacobibanez.godot.gpgs.loadAchievementsSuccess
import com.jacobibanez.godot.gpgs.revealAchievementFailure
import com.jacobibanez.godot.gpgs.revealAchievementSuccess
import com.jacobibanez.godot.gpgs.unlockAchievementFailure
import com.jacobibanez.godot.gpgs.unlockAchievementSuccess
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

class AchievementsProxy(
    private val godot: Godot,
    private val achievementsClient: AchievementsClient = PlayGames.getAchievementsClient(godot.activity!!)
) {

    private val tag: String = AchievementsProxy::class.java.simpleName

    private val showAchievementsRequestCode = 9001

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

    fun loadAchievements(forceReload: Boolean) {
        Log.d(tag, "Loading achievements")
        achievementsClient.load(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievements loaded successfully")
                Log.d(tag, "Achievements are stale? ${task.result.isStale}")
                Log.d(tag, "Number of achievements: ${task.result.get()?.count}")

                val achievements: List<Dictionary> =
                    if (task.result.get() != null && task.result.get()!!.count > 0) {
                        task.result.get()!!.map { fromAchievement(it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(godot, PLUGIN_NAME, loadAchievementsSuccess, Gson().toJson(achievements))
            } else {
                Log.e(tag, "Failed to load achievements. Cause: ${task.exception}", task.exception)
                emitSignal(godot, PLUGIN_NAME, loadAchievementsFailure)
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
            ActivityCompat.startActivityForResult(
                godot.activity!!, intent,
                showAchievementsRequestCode, null
            )
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