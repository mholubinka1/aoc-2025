import java.io.File

fun main() {
    val testInput = """
         123 328  51 64 
          45 64  387 23 
           6 98  215 314
         *   +   *   +  
    """.trimIndent().split("\n")
    var input = testInput
    
    val filePath = "input.txt"
    input = File(filePath).readLines()

    val width = input.maxOf { it.length }
    val matrix: List<List<Char>> = input.map { line ->
        line.padEnd(width, ' ').toList()
    }

    var maxCol = width - 1
    var col = maxCol

    var total = 0L

    while (true) {
        var operation: String? = null
        var numbers = mutableListOf<Long>()
        while (true) {
            if (col < 0) {
                break
            }
            var columnChars = mutableListOf<Char>()
            for (row in matrix.size - 1 downTo 0) {
                val ch = matrix[row][col]
                if (ch == ' ') {    
                    continue
                }
                if (ch == '+' || ch == '*') {
                    operation = ch.toString()
                    continue
                }              
                columnChars.add(matrix[row][col])
            }
            columnChars.reverse()       
            val numberStr = columnChars.joinToString("")
            var number = numberStr.toLongOrNull()
            if (number != null) {
                numbers.add(numberStr.toLong())
                println("Column ${col}: $numberStr")
                col --
            }
            else {
                col --
                break
            }
        }
        println ("Operation: $operation")
        when (operation) {
            "+" -> {
                var columnSum = numbers.sum()
                println("Column sum: $columnSum")
                total += columnSum
            }
            "*" -> {
                var columnProduct = numbers.reduce { acc, n -> acc * n }
                println("Column product: $columnProduct")
                total += columnProduct
            }
            
        }
        if (col < 0) {
            break
        }
    }
    println("Total: $total")
    
    //var columns = input.first().size
    //var total = 0L
    
    /*for (column in 0 until columns) {
        var operation = input.last()[column]
        var numbers = input.subList(0, input.size - 1).map { row ->
            row[column].toLong()
        }
        when (operation) {
            "+" -> {
                var columnSum = numbers.sum()
                println("Column $column sum: $columnSum")
                total += columnSum
            }
            "*" -> {
                var columnProduct = numbers.reduce { acc, n -> acc * n }
                println("Column $column product: $columnProduct")
                total += columnProduct
            }
        }
    }
    println("Total: $total")*/


}
