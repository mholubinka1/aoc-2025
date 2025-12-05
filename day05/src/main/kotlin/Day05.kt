import java.io.File

data class Range(
    val start: Long, 
    val end: Long
)

data class Ranges(
    val ranges: List<Range>
) {
    fun contains(value: Long): Boolean {
        for (range in ranges) {
            if (value in range.start..range.end) {
                return true
            }
        }
        return false
    }

    fun total(): Long {
        var sorted = ranges.sortedBy { it.start }

        var mergedStart = sorted[0].start
        var mergedEnd = sorted[0].end       

        var total = 0L

        for (i in 1 until sorted.size) {
            val range = sorted[i]
            if (range.start <= mergedEnd + 1) {
                mergedEnd = maxOf(mergedEnd, range.end)
            } else {
                total += (mergedEnd - mergedStart + 1)
                mergedStart = range.start
                mergedEnd = range.end
            }
        }
        total += (mergedEnd - mergedStart + 1)              
        return total
    }
}

fun parse_input(lines: List<String>): Pair<Ranges, List<Long>> {
    val rangeLines = mutableListOf<String>()
    val valueLines = mutableListOf<String>()
    var isRangeSection = true

    for (line in lines) {
        if (line.isBlank()) {
            isRangeSection = false
            continue
        }
        if (isRangeSection) {
            rangeLines.add(line)
        } else {
            valueLines.add(line)
        }
    }

    val ranges = rangeLines.map { line ->
        val parts = line.split("-")
        Range(parts[0].toLong(), parts[1].toLong())
    }

    val values = valueLines.map { it.toLong() }

    return Pair(Ranges(ranges), values)
}

fun main() {
    var testInput = """
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
    """.trimIndent().split("\n")
    var input = parse_input(testInput)

    val filePath = "input.txt"
    input = parse_input(File(filePath).readLines())

    val ranges = input.first
    val values = input.second

    var validCount = 0
    for (value in values) {
        if (ranges.contains(value)) {
            validCount++
       }
    }
    var total = ranges.total()

    println("Fresh Ingredient IDs: $total")
}
