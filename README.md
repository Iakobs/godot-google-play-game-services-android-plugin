# Google Play Game Services for Godot

[![Android](https://img.shields.io/badge/Platform-Android%20API%20Level%2033-brightgreen.svg)](https://developer.android.com)
[![Godot](https://img.shields.io/badge/Godot%20Engine-3.5.2-blue.svg)](https://github.com/godotengine/godot/)
[![PGS](https://img.shields.io/badge/Play%20Games%20Services%20v2-17.0.0-green.svg)](https://developers.google.com/games/services/android/quickstart)

> :warning: In order to use the Google Play Game Services, you will need a Google developer account, which involves a **once in a lifetime payment of $25** at the time of writing (April 2023)

## Credit and Acknowledgments

This plugin started as an update of [cgisca's plugin](https://github.com/cgisca/PGSGP). 

[@rafalagoon](https://github.com/rafalagoon) suggested to update the plugin since it was not working with godot versions 3.3.x or later, and Google has provided a complete revamp of the integration with their game services.

As the task to update cgisca's plugin involved learning how to use the new Google Play Game Services library, the android library as well, and a bunch of things completeley new to me, I decided to start from scratch in this new repository.

## Table of contents

* [Purpose](#purpose)
  * [Supported features](#supported-features)
* [Side note](#side-note)
* [Preparing everything](#preparing-everything)
  * [In this repository](#in-this-repository)
  * [In Godot](#in-godot)
  * [In your Google account](#in-your-google-account)
* [Detailed How To's](#detailed-how-tos)

## Purpose

This is an android plugin for godot to integrate [Google Play Game Services](https://developers.google.com/games/services) in your games. It is not intended to be an exhaustive integration because Google Play Game Services has many APIs and some of the DTOs being returned are not compatible with Godot or difficult to map from one system to the other.

That being said, we've tried to include as much as we thought it was necessary to provide a professional integration with Google's system.

Also, this plugin is still a work in progress, so bear with us as we introduce more integrations.

### Supported features

So far, the plugin provides the following features:

* Achievements
  * Display
  * Unlock
  * Increment
  * Reveal
  * Load
* Leaderboards
  * Display all
  * Display one
  * Display one based on time span
  * Display one based on time span and collection
  * Submit scores

## Side note

This plugin has a [godot plugin counterpart](https://github.com/Iakobs/godot-google-play-game-services-demo) that provides code completion in your godot editor amongst other things. While not necessary, the godot plugin will help you integrate this android plugin in your project, and it also comes with a demo godot project.

## Preparing everything

In order to use this plugin in your godot game, you have to follow some steps. **It might seem tedious and daunting**, but I have tried to provide comprehensive instructions.

These are the general steps overall, for the specific how to's on every step, keep reading.

### In this repository
1. Download the android library for godot and paste it in the proper folder
2. Build the project with gradle to generate the .aar file
3. Copy the resulting .aar file and the existing .gdap file for later

### In Godot
1. Create a new godot project
2. Configure your editor android settings
3. Download an android template
4. Create an android run export configuration
5. Paste the plugin .aar and .gdap files in the proper folder
6. Modify the android template accordingly

### In your Google account
1. Create a Google Play Console dev account (this is the step involving a payment)
2. Create a new app of type game
3. Configure your achievements, leaderboards, etc.

## Detailed How To's

(Work in progress)
