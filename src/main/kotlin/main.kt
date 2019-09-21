import org.w3c.dom.Element
import org.w3c.dom.asList
import kotlin.browser.document


val prefix = "cndr2"
val newPrefix = "c2-revive"

/**
 * Entry-point of the extension.
 */
fun main(args: Array<String>) {
    getRows().let { rows ->
        removeUnwantedButtons(rows)
        addVrButtons(rows)
    }
    colourPlayerCounts()
    colourPasswords()
    colourStatuses()
    colourDistances()
    colourLandscapes()
    colourUptimes()
}

/**
 * Returns all the rows in the table as Elements.
 */
fun getRows(): List<Element> {
    return document.getElementsByTagName("tr")
        .asList()
        .filter { it.getAttribute("role") == "row" }
}

/**
 * Removes all buttons from all supplied rows.
 */
fun removeButtons(rows: List<Element>) {
    rows.toTypedArray()
        .forEach {
            it.getButtons().forEach { b -> b.parentNode?.removeChild(b) }
        }
}

/**
 * Sets the background colours of the uptime sections to white.
 */
fun colourUptimes() {
    document.getElementsByClassName("column-deltatime")
        .asList()
        .forEach { elem ->
            js("$declaration'#ffffff';")
        }
}

/**
 * Sets the background colours of the landscape sections to white.
 */
fun colourLandscapes() {
    document.getElementsByClassName("column-landscape")
        .asList()
        .forEach { elem ->
            js("$declaration'#ffffff';")
        }
}

/**
 * Sets the background colours of the task length sections according to the
 * distance.
 */
fun colourDistances() {
    val distCols = document.getElementsByClassName("column-taskdescription").asList()
    val distances = distCols.map {
        it.innerHTML.replace("km", "")
            .trim()
            .toIntOrNull() ?: 0
    }.toTypedArray()
    val maxValue = distances.max() ?: 10
    val handler = DistanceColourHandler(maxValue)

    distCols.forEachIndexed { i, elem ->
        if (i >= 2) { // Because the first two items are not actually player counts.
            val value = distances[i]
            handler.apply(value, elem)
        }
    }
}

/**
 * Sets the background colours of the server status sections to show which
 * servers can be joined.
 */
fun colourStatuses() {
    document.getElementsByClassName("column-serverstatus")
        .asList()
        .forEach { elem ->
            when (elem.innerHTML) {
                "Joining Enabled" -> js("$declaration'#$green'")
                "Waiting for Race Start" -> js("$declaration'#$lightRed'")
                "Race in Progress" -> js("$declaration'#$red'")
            }
        }
}

fun colourPasswords() {
    document.getElementsByClassName("column-passprotected")
        .asList()
        .forEach { elem ->
            if (elem.innerHTML == "No") {
                js("$declaration'#$green';")
            } else if (elem.innerHTML == "Yes") {
                js("$declaration'#$red';")
            }
        }
}

/**
 * Sets the background colours of the player count sections according to the
 * number of players.
 */
fun colourPlayerCounts() {
    val players = document.getElementsByClassName("column-playersnum").asList()
    val counts = players.map {
        it.innerHTML.split("/").firstOrNull()?.toIntOrNull() ?: 0
    }.toTypedArray()
    val maxValue = counts.max() ?: 1

    val handler = PlayerCountColourHandler(maxValue)
    players.toTypedArray().forEachIndexed { i, elem ->
        if (i >= 2) { // Because the first two items are not actually player counts.
            val value = counts[i]
            handler.apply(value, elem)
        }
    }
}

/**
 * Modifies the link of a particular element to point to the
 * Condor2 Revive Helper.
 */
fun modifyLink(elem: Element) {
    elem.href = elem.href.replace(prefix, newPrefix)
}

/**
 * Removes the "Join" button from any rows which are not
 * available to join.
 */
fun removeUnwantedButtons(rows: List<Element>) {
    rows.toTypedArray()
        .filter { !availableToJoin(it) }
        .let { removeButtons(it) }
}

/**
 * Adds a "Join (VR)" button to each row which has a button.
 */
fun addVrButtons(rows: List<Element>) {
    rows.toTypedArray()
        .forEach { row ->
            // Add buttons for VR and set their links.
            row.getElementsByClassName("column-serverlink")
                .asList()
                .firstOrNull()
                ?.let { e ->
                    val newColumn = e.cloneNode(true)
                    row.insertBefore(newColumn, e)

                    row.getElementsByTagName("a")
                        .asList()
                        .firstOrNull()
                        ?.let {
                            modifyLink(it)
                        }
                }
            // Set the text of the VR buttons.
            row.getButtons()
                .firstOrNull()
                ?.let { b -> b.innerHTML = "Join (VR)" }
        }
}

/**
 * Returns whether it is possible to join a server.
 */
fun availableToJoin(row: Element): Boolean {
    val status = row.getElementsByClassName("column-serverstatus")
        .asList()
        .firstOrNull()?.innerHTML
    return status != "Race in Progress"
}

/**
 * Returns a list of all buttons belonging to an element.
 */
fun Element.getButtons(): List<Element> {
    return getElementsByTagName("button").asList()
}

var Element.href: String
    get() = this.getAttribute("href") ?: ""
    set(value) = this.setAttribute("href", value)
