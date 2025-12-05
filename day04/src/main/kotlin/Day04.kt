import java.io.File

fun countNeighbours(grid: Array<ByteArray>, x: Int, y: Int): Int {
    val rows = grid.size
    val cols = grid[0].size

    var ones = 0

    val directions = arrayOf(
        -1 to -1, -1 to 0, -1 to 1,
         0 to -1,          0 to 1,
         1 to -1,  1 to 0, 1 to 1
    )

    for ((dx, dy) in directions) {
        val nx = x + dx
        val ny = y + dy
        if (nx in 0 until rows && ny in 0 until cols) {
            if (grid[nx][ny] == 1.toByte()) {
                ones++
            }
        }
    }
    return ones
}

fun removeRolls(grid: Array<ByteArray>, coordinates: List<Pair<Int, Int>>) {
    for ((x, y) in coordinates) {
        grid[x][y] = 0.toByte()
    }
}

fun main() {
    val testInput = """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
    """.trimIndent().split("\n")
    var input: Array<ByteArray> = testInput.map { line ->
        line.map { ch -> if (ch == '@') 1.toByte() else 0.toByte() }.toByteArray()
    }.toTypedArray()

    val filePath = "input.txt"
    input = File(filePath).readLines().map { line ->
        line.map { ch -> if (ch == '@') 1.toByte() else 0.toByte() }.toByteArray()
    }.toTypedArray()

    val neighbours = 4
    var totalAccessible = 0
    
    var rollsToRemove = true
    while (rollsToRemove) {
        var accessible = 0
        var coordinatesToRemove = mutableListOf<Pair<Int, Int>>()
        for (x in input.indices) {
            for (y in input[0].indices) {
                val n = countNeighbours(input, x, y)
                if (n < neighbours && input[x][y] == 1.toByte()) {
                    coordinatesToRemove.add(x to y)
                    accessible++
                }
            }
        }
        totalAccessible += accessible
        println("Accessible in this pass: $accessible")
        if (coordinatesToRemove.isEmpty()) {
            rollsToRemove = false
        }
        else{
            removeRolls(input, coordinatesToRemove)
        }
    }
    println("Total Accessible paper rolls: $totalAccessible")
}
