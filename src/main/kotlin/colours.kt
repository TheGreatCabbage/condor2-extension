import org.w3c.dom.Element
import kotlin.math.max

// Compile-time constant strings to use in `js(...)` calls.
const val declaration = "elem.style.backgroundColor = "
const val lim = 9 // The highest colour index for shades of orange/blue.

const val orange0 = "fff0e6"
const val orange1 = "ffe0cc"
const val orange2 = "ffd1b3"
const val orange3 = "ffc299"
const val orange4 = "ffb380"
const val orange5 = "ffa366"
const val orange6 = "ff944d"
const val orange7 = "ff8533"
const val orange8 = "ff751a"
const val orange9 = "ff6600"

const val blue0 = "e6f2ff"
const val blue1 = "cce6ff"
const val blue2 = "b3d9ff"
const val blue3 = "99ccff"
const val blue4 = "80bfff"
const val blue5 = "66b3ff"
const val blue6 = "4da6ff"
const val blue7 = "3399ff"
const val blue8 = "1a8cff"
const val blue9 = "0080ff"

const val green = "66cc99"
const val red = "ff4d4d"
const val lightRed = "ff8080"

/**
 * Gets the index of the colour to use for a particular value,
 * when the colour depends on the value and the maximum value.
 */
fun getColourIndex(value: Int, maxValue: Int): Int {
    val range = 0..lim step max(lim / maxValue, 1)
    val result = range.takeWhile { it <= value * lim / maxValue }.lastOrNull() ?: 0

    // If there are more than 0 players but the result is 0, use a slightly darker colour to make it clear.
    if (result == 0 && value > 0) {
        return 1
    }
    return result
}

/**
 * A class which helps with applying colours to objects.
 */
abstract class ColourHandler(val maxValue: Int) {
    abstract fun apply(value: Int, elem: Element)
}

class PlayerCountColourHandler(maxValue: Int) : ColourHandler(maxValue) {

    override fun apply(value: Int, elem: Element) {
        when (getColourIndex(value, maxValue)) {
            // Unfortunately, we can't use a list or map etc. because the `js(...)` call
            // must be supplied with a compile-time constant string.
            0 -> js("$declaration'#$orange0';")
            1 -> js("$declaration'#$orange1';")
            2 -> js("$declaration'#$orange2';")
            3 -> js("$declaration'#$orange3';")
            4 -> js("$declaration'#$orange4';")
            5 -> js("$declaration'#$orange5';")
            6 -> js("$declaration'#$orange6';")
            7 -> js("$declaration'#$orange7';")
            8 -> js("$declaration'#$orange8';")
            9 -> js("$declaration'#$orange9';")
        }
    }
}

class DistanceColourHandler(maxValue: Int) : ColourHandler(maxValue) {

    override fun apply(value: Int, elem: Element) {
        when (getColourIndex(value, maxValue)) {
            // Unfortunately, we can't use a list or map etc. because the `js(...)` call
            // must be supplied with a compile-time constant string.
            0 -> js("$declaration'#$blue0';")
            1 -> js("$declaration'#$blue1';")
            2 -> js("$declaration'#$blue2';")
            3 -> js("$declaration'#$blue3';")
            4 -> js("$declaration'#$blue4';")
            5 -> js("$declaration'#$blue5';")
            6 -> js("$declaration'#$blue6';")
            7 -> js("$declaration'#$blue7';")
            8 -> js("$declaration'#$blue8';")
            9 -> js("$declaration'#$blue9';")
        }
    }
}