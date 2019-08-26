import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.browser.window

const val MAJOR_VERSION = 1
const val MINOR_VERSION = 2

const val RELEASES_URL = "https://api.github.com/repos/thegreatcabbage/condor2-extension/releases"

/**
 * Checks for updates using the GitHub Releases API. If an update is found,
 * a message is shown via a link on the Condor Server List website.
 */
fun checkForUpdates() {
    window.fetch(RELEASES_URL).then {
        it.json().then {
            val tags = (it as Array<*>).map {
                val item: dynamic = it
                val name = item.tag_name.toString()
                name.split("v").last()
            }
            if (isHigherVersion(tags)) {
                showUpdateMessage()
            }
        }
    }.catch { console.log("Condor2 Extension: check for updates failed.") }
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
 * Returns whether any of the release tags are higher than the current release.
 */
fun isHigherVersion(tags: List<String>): Boolean {
    return tags.map {
        val (majorVersion, minorVersion) = it.split(".").map { it.toInt() }
        val newer = majorVersion > MAJOR_VERSION || (minorVersion > MINOR_VERSION && majorVersion == MAJOR_VERSION)
        newer
    }.any { it }
}