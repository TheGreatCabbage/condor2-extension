# Condor2 Revive Extension

## Introduction

Condor2 Extension is a browser extension which enhances the [Condor Server List](http://www.condorsoaring.com/serverlist/?wdt_search=cndr2). It greatly simplifies the process of finding a server by colour-coding columns, and adds buttons which allow VR users to seamlessly launch multiplayer sessions using Revive when the [Condor2 Revive Helper](https://github.com/TheGreatCabbage/condor2-revive-helper) is installed.

## Installing

For Firefox, the extension can be installed from [here](https://addons.mozilla.org/en-GB/firefox/addon/condor2extension/) on the official Firefox addons website.

## Comparison

#### Condor Server List
![Screenshot of Condor Server List without extension installed](images/screenshot_original.png)

#### Condor Server List with the extension installed
![Screenshot of Condor Server List with the extension installed](images/screenshot_new.png)

## Building From Source

This extension is written in Kotlin/JS. To build it, execute the command `./gradlew runDceKotlin` in the root directory. You can then use `web-ext run` to test the plugin.

After building, you can package the plugin as an `.xpi` file by running `python package.py` (currently requires 7-Zip and Windows).
