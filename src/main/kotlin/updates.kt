import org.w3c.dom.Navigator
import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

const val MAJOR_VERSION = 1
const val MINOR_VERSION = 2

const val RELEASES_URL = "https://api.github.com/repos/thegreatcabbage/condor2-extension/releases"
const val INTERVAL_MILLIS = 1e3 * 3600 * 12

external val chrome: Chrome

external class Chrome {
    val storage: Storage
}

external class Storage {
    val local: Local
}

external class Local {
    fun get(key: Array<String>, func: (dynamic) -> dynamic): dynamic
    fun set(obj: dynamic, func: (dynamic) -> dynamic)
}

/**
 * Checks for updates using the GitHub Releases API. If an update is found,
 * a message is shown via a link on the Condor Server List website.
 */
fun checkForUpdates() {
    // Firefox extension will update automatically, so don't check for updates.
    if (isFirefox()) {
        return
    }

    chrome.storage.local.get(arrayOf("updateTime", "latestVersion")) {
        val item = it
        if (item.updateTime == undefined) {
            item.updateTime = 0
        }

        val currentTime = Date.now().asDynamic()
        val timeSinceLastCheck = currentTime - item.updateTime
        console.log("Condor2 Extension: Last checked for updates ${timeSinceLastCheck / 1e3} seconds ago.")

        if (timeSinceLastCheck > INTERVAL_MILLIS) {
            window.fetch(RELEASES_URL).then { response ->
                response.json().then {
                    val tags = (it as Array<*>).map {
                        it.asDynamic()
                            .tag_name
                            .toString()
                            .split("v")
                            .last()
                    }
                        .flatMap { it.split(".") }
                        .map { it.toInt() }
                        .chunked(2)
                        .map { it.first() to it.last() }

                    val latest = getHighestVersion(tags)
                    if (isHigherVersion(latest)) {
                        showUpdateMessage()
                        console.log("Condor2 Extension found new version.")
                    } else {
                        console.log("Condor2 Extension is the latest version.")
                    }

                    item.updateTime = currentTime
                    item.latestVersion = "${latest.first}.${latest.second}"
                    chrome.storage.local.set(item) {}
                }
            }.catch { console.log("Condor2 Extension: check for updates failed.") }
        } else {
            val version = item.latestVersion.toString()
                .split(".")
                .map { it.toIntOrNull() ?: -1 }
                .filter { it != -1 }

            if (version.size == 2 && isHigherVersion(version.zipWithNext().first())) {
                console.log("Condor2 Extension: found new version, v${version.first()}.${version.last()}")
                showUpdateMessage()
            } else {
                console.log("Condor2 Extension was latest version on last check.")
            }
        }
    }
}

external val navigator: Navigator

fun isFirefox(): Boolean {
    return navigator.userAgent.toLowerCase().indexOf("firefox") > -1
}

/**
 * Shows a message to update to a newer version of the extension.
 */
fun showUpdateMessage() {
    document.getElementsByClassName("fusion-column-wrapper")
        .asList()
        .firstOrNull()
        ?.let {
            val updateMsg = document.createElement("a")
            js("updateMsg.style.fontSize = '18px';")
            js("updateMsg.style.color = 'red';")
            js("updateMsg.align = 'center';")
            updateMsg.href = "https://github.com/TheGreatCabbage/condor2-extension/releases"
            updateMsg.innerHTML = "A newer version of the Condor2 Extension is now available. Click to download."
            it.append(updateMsg)
        }
}

/**
 * Returns whether a version consisting of a major and minor version is higher than the current version.
 */
fun isHigherVersion(tag: Pair<Int, Int>): Boolean {
    val (majorVersion, minorVersion) = tag
    return majorVersion > MAJOR_VERSION || (minorVersion > MINOR_VERSION && majorVersion == MAJOR_VERSION)
}

/**
 * Gets the highest version from a list of major/minor version pairs.
 */
fun getHighestVersion(tags: List<Pair<Int, Int>>): Pair<Int, Int> {
    return tags.sortedWith(compareBy({ it.first }, { it.second })).last()
}