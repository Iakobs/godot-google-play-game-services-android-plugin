package com.jacobibanez.godot.gpgs

import android.util.Log
import com.google.android.gms.games.GamesSignInClient
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot

class GodotGooglePlayGameServices(godot: Godot) : GodotPlugin(godot) {

    private val tag: String = GodotGooglePlayGameServices::class.java.simpleName

    private lateinit var gamesSignInClient: GamesSignInClient

    override fun getPluginName(): String {
        return "GodotGooglePlayGameServices"
    }

    /**
     * Use this to test that the plugin works
     */
    @UsedByGodot
    fun helloWorld() {
        Log.i(tag,"Hello World")
    }

    @UsedByGodot
    fun initialize() {
        Log.i(tag, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)

        gamesSignInClient = PlayGames.getGamesSignInClient(activity!!)
    }
}