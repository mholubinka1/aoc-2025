import java.io.File


class Bag<T>() {
    private val elements = mutableMapOf<T, Long>()

    fun add(value: T, count: Long? = null) {
        val increment = count ?: 1
        elements[value] = (elements[value] ?: 0) + increment
    }

    fun remove(value: T) {
        elements.remove(value)
    }

    fun contains(value: T): Boolean {
        return value in elements
    }

    fun count(value: T): Long {
        return elements[value] ?: 0
    }

    fun write() {
        println(elements)
    }
}


fun main() {
    val testInput = """
    .......S.......
    ...............
    .......^.......
    ...............
    ......^.^......
    ...............
    .....^.^.^.....
    ...............
    ....^.^...^....
    ...............
    ...^.^...^.^...
    ...............
    ..^...^.....^..
    ...............
    .^.^.^.^.^...^.
    ...............
    """.trimIndent().split("\n")
    var input: List<List<Int>> = testInput.map { line -> 
        line.map { ch -> 
            when (ch) {
                '.' -> 0
                'S' -> 1
                '^' -> 2
                else -> error("Invalid character: $ch")
            }
        }
    }

    val filePath = "input.txt"
    input = File(filePath).readLines().map { line -> 
        line.map { ch -> 
            when (ch) {
                '.' -> 0
                'S' -> 1
                '^' -> 2
                else -> error("Invalid character: $ch")
            }
        }
    }

    var beamPositions = Bag<Int>()
    var timelines = 1L
    var splits = 0L

    var lineCount = 0L
    for (line in input) {
        var splitsChange = 0L
        for (i in 0..line.size - 1) {
            val cell = line[i]
            if (cell == 1) {
                println("Beam at position $i")
                beamPositions.add(i)
            }
            if (cell == 2) {
                if (beamPositions.contains(i)) {
                    splits++
                    var beamCount = beamPositions.count(i)
                    splitsChange += beamCount
                    beamPositions.remove(i)
                    beamPositions.add(i+1, beamCount)
                    beamPositions.add(i-1, beamCount)
                    //beamPositions.write()
                }
            }
        }
        println("Splits change = $splitsChange")
        timelines += splitsChange
        lineCount++
    }
    println("Total timelines: $timelines")
    println("Total splits: $splits")            
}
