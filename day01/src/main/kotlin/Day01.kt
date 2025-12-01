import java.io.File
import kotlin.math.abs

data class CircularResult(
    val currentValue: Int,
    val crossed: Int
)

fun addCircular(a: Int, b: Int): CircularResult {
    var modulus = 100
    var remainder = b % modulus
    val quotient = abs(b / modulus)

    val result =  ((a + remainder) % modulus + modulus) % modulus

    var crossed = when {
        result == 0 -> 1
        remainder > 0 && remainder > (modulus - a) -> 1
        remainder < 0 && abs(remainder) > a && a != 0 -> 1
        else -> 0
    }

    crossed = crossed + quotient
    println("safe = $b, quotient = $quotient, remainder = $remainder")
    println("$a -> $result, zero count = $crossed")
    return CircularResult(result, crossed)
}

fun main() {
    val testInput = intArrayOf(-68, -30, 48, -5, 60, -55, -1, -99, 14, -82)
    val filePath = "input.txt"

    val input: List<Int> = File(filePath).readLines().mapNotNull { line ->
        val number = line.drop(1).toIntOrNull() ?: return@mapNotNull null
        when (line.firstOrNull()) {
            'L' -> -number
            'R' -> number
            else -> null
        }
    }

    val safeStartingValue = 50
    var safeCurrentValue = safeStartingValue

    var zeroCount = 0

    for (number in input) {
        val result = addCircular(safeCurrentValue, number)
        safeCurrentValue = result.currentValue
        zeroCount = zeroCount + result.crossed
    }
    println("$zeroCount")
}
