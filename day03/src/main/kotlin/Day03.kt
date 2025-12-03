import java.io.File

data class Digit(
    val value: Int,
    val position: Int
)

fun findMaxDigitInSubarray(subArray: List<Int>, positionOffset: Int): Digit {
    var maxDigit: Digit? = null
    for ((index, value) in subArray.withIndex()) {
        val originalIndex = index + positionOffset
        if (maxDigit == null || value > maxDigit.value) {
            maxDigit = Digit(value, originalIndex)
        }
    }
    if (maxDigit != null) {
        println("Max digit in subarray $subArray is ${maxDigit.value} at original position ${maxDigit.position}")
    }
    return maxDigit!!
}


fun main() {
    val testInput = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent().split("\n")
    var input: List<List<Int>> = testInput.map { line ->
        line.mapNotNull { ch -> ch.toString().toIntOrNull() }
    }

    val filePath = "input.txt"
    input = File(filePath).readLines().map { line ->
        line.mapNotNull { ch -> ch.toString().toIntOrNull() }
    }
    
    var digits = 12
    val joltages: MutableList<Long> = mutableListOf()

    for (array in input) {
        var numberStr = ""
        var foundDigits: MutableList<Digit> = mutableListOf()
        for (i in 1..digits) {
            var startIndex = if (foundDigits.isEmpty()) {
                0
            }
            else {
                foundDigits.last().position + 1
            }
            var endIndex = array.size - (digits - i)
            val subArray = array.subList(startIndex, endIndex)

            val maxDigit = findMaxDigitInSubarray(subArray, startIndex)
            foundDigits.add(maxDigit)
            numberStr += maxDigit.value.toString()
        }
        println("Max $digits number: $numberStr")
        joltages.add(numberStr.toLong())
    }

    val sum = joltages.sum()
    println("Total Output Joltage: $sum")
}





