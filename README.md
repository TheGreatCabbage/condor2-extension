# Condor2 Revive Extension

## Introduction

Condor2 Revive Extension is a browser extension to be used in conjunction with the [Condor2 Revive Helper](https://github.com/TheGreatCabbage/condor2-revive-helper). It modifies the links on the [Condor Server List](http://www.condorsoaring.com/serverlist/?wdt_search=cndr2) to point to the Condor2 Revive Helper instead of Condor2.

## Building From Source

This extension is written in Kotlin/JS. To build it, execute the command `./gradlew runDceKotlin` in the root directory. You can then use `web-ext run` to test the plugin.

After building, you can package the plugin as an `.xpi` file by running `python package.py` (currently requires 7-Zip and Windows).
