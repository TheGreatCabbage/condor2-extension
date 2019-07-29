import org.w3c.dom.Element
import org.w3c.dom.asList
import kotlin.browser.document

val prefix = "cndr2"
val newPrefix = "c2-revive"

fun main(args: Array<String>) {
    document.getElementsByTagName("a")
        .asList()
        .filter { it.href.startsWith(prefix) }
        .toTypedArray() // This is necessary.
        .forEach { it.href = it.href.replace(prefix, newPrefix) }
}

var Element.href: String
    get() = this.getAttribute("href") ?: ""
    set(value) = this.setAttribute("href", value ?: "")