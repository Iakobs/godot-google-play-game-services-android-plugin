package com.jacobibanez.godot.gpgs.achievements

import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.achievement.AchievementBuffer
import com.google.gson.Gson
import com.jacobibanez.godot.gpgs.PLUGIN_NAME
import com.jacobibanez.godot.gpgs.signals.AchievementsSignals.achievementsRevealed
import com.jacobibanez.godot.gpgs.signals.AchievementsSignals.achievementsUnlocked
import com.jacobibanez.godot.gpgs.signals.AchievementsSignals.achievementsLoaded
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal

/** @suppress */
class AchievementsProxy(
    private val godot: Godot,
    private val achievementsClient: AchievementsClient = PlayGames.getAchievementsClient(godot.getActivity()!!)
) {

    private val tag: String = AchievementsProxy::class.java.simpleName

    private val showAchievementsRequestCode = 9001

    fun increment(achievementId: String, amount: Int, immediate: Boolean) {
        val immediateText = if (immediate) "(immediately)" else ""
        Log.d(tag, "Incrementing achievement with id $achievementId in an amount of $amount $immediateText")

        if (!immediate) {
            achievementsClient.increment(achievementId, amount)
        }

        achievementsClient.incrementImmediate(achievementId, amount).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Achievement $achievementId incremented successfully. Unlocked? ${task.result}"
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsUnlocked,
                    task.result,
                    achievementId
                )
            } else {
                Log.e(
                    tag,
                    "Achievement $achievementId not incremented. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsUnlocked,
                    false,
                    achievementId
                )
            }
        }
    }

    fun load(forceReload: Boolean) {
        Log.d(tag, "Loading achievements")
        achievementsClient.load(forceReload).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Achievements loaded successfully. Data is stale? ${task.result.isStale}"
                )
                val safeBuffer: AchievementBuffer = task.result.get()!!
                val achievementsCount = safeBuffer.count
                val achievements: List<Dictionary> =
                    if (achievementsCount > 0) {
                        safeBuffer.map { fromAchievement(godot, it) }.toList()
                    } else {
                        emptyList()
                    }

                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsLoaded,
                    Gson().toJson(achievements)
                )
            } else {
                Log.e(tag, "Failed to load achievements. Cause: ${task.exception}", task.exception)
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsLoaded,
                    Gson().toJson(emptyList<Dictionary>())
                )
            }
        }
    }

    fun reveal(achievementId: String, immediate: Boolean) {
        val immediateText = if (immediate) "(immediately)" else ""
        Log.d(tag, "Revealing achievement with id $achievementId $immediateText")

        if (!immediate) {
            achievementsClient.reveal(achievementId)
            return
        }

        achievementsClient.revealImmediate(achievementId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievement $achievementId revealed")
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsRevealed,
                    true,
                    achievementId
                )
            } else {
                Log.e(
                    tag,
                    "Achievement $achievementId not revealed. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsRevealed,
                    false,
                    achievementId
                )
            }
        }
    }

    fun setSteps(achievementId: String, amount: Int, immediate: Boolean) {
        val immediateText = if (immediate) "(immediately)" else ""
        Log.d(tag, "Setting steps for achievement with id $achievementId in an amount of $amount $immediateText")

        if (!immediate) {
            achievementsClient.setSteps(achievementId, amount)
            return
        }

        achievementsClient.setStepsImmediate(achievementId, amount).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    tag,
                    "Achievement $achievementId steps set successfully. Unlocked? ${task.result}"
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsUnlocked,
                    task.result,
                    achievementId
                )
            } else {
                Log.e(
                    tag,
                    "Achievement $achievementId not incremented. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsUnlocked,
                    false,
                    achievementId
                )
            }
        }
    }

    fun show() {
        Log.d(tag, "Showing achievements")
        achievementsClient.achievementsIntent.addOnSuccessListener { intent ->
            ActivityCompat.startActivityForResult(
                godot.getActivity()!!, intent,
                showAchievementsRequestCode, null
            )
        }
    }

    fun unlock(achievementId: String, immediate: Boolean) {
        val immediateText = if (immediate) "(immediately)" else ""
        Log.d(tag, "Unlocking achievement with id $achievementId $immediateText")

        if (!immediate) {
            achievementsClient.unlock(achievementId)
            return
        }

        achievementsClient.unlockImmediate(achievementId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(tag, "Achievement with id $achievementId unlocked")
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsUnlocked,
                    true,
                    achievementId
                )
            } else {
                Log.e(
                    tag,
                    "Error unlocking achievement $achievementId. Cause: ${task.exception}",
                    task.exception
                )
                emitSignal(
                    godot,
                    PLUGIN_NAME,
                    achievementsUnlocked,
                    false,
                    achievementId
                )
            }
        }
    }
}
