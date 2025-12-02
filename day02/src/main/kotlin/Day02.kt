import java.io.File

data class IdRange(
    var min: Long,
    var max: Long
)

fun strToRanges(input: String): List<IdRange> {
    return input.split(",").mapNotNull { part ->
        val bounds = part.split("-")
        if (bounds.size != 2) return@mapNotNull null
        val min = bounds[0].toLongOrNull() ?: return@mapNotNull null
        val max = bounds[1].toLongOrNull() ?: return@mapNotNull null
        IdRange(min, max)
    }
}

fun main() {
    val testInput = """
        11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
        1698522-1698528,446443-446449,38593856-38593862,565653-565659,
        824824821-824824827,2121212118-2121212124
    """.trimIndent().replace("\n", "")
    //val input: List<IdRange> =  strToRanges(testInput)
    
    val filePath = "input.txt"
    val input: List<IdRange> =  strToRanges(File(filePath).readText().trim())
    
    var invalidIds = mutableListOf<Long>()

    for (range in input) {
        for (i in range.min..range.max) {
            val idStr = i.toString()
            val strLen = idStr.length
            for (subLen in 1..(strLen / 2)) {
                if (strLen % subLen != 0) {
                    continue
                }
                val subId = idStr.substring(0, subLen)
                val repeated = subId.repeat(strLen / subLen)
                if (repeated == idStr) {
                    println("Invalid ID found: $idStr")
                    invalidIds.add(i)
                    break
                }
            }
        }
    }

    val sum = invalidIds.sum()
    println("Sum of invalid IDs: $sum")
}
