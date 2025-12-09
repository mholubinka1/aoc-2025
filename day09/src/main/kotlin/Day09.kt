import java.io.File

data class Edge(
    val c: Long, 
    val min: Long, 
    val max: Long
)

data class Rectangle(
    val minX: Long,
    val minY: Long,
    val maxX: Long,
    val maxY: Long
)

fun splitEdges(redTiles: List<Pair<Long, Long>>): Pair<List<Edge>, List<Edge>> {
    val vertical = mutableListOf<Edge>()
    val horizontal = mutableListOf<Edge>()

    for (i in 0 until redTiles.size) {
        val start = redTiles[i]
        val end = redTiles[(i + 1) % redTiles.size]

        if (start.first == end.first) {
            vertical.add(
                Edge(
                    start.first,
                    minOf(start.second, end.second),
                    maxOf(start.second, end.second)
                )
            )
        } else if (start.second == end.second) {
            horizontal.add(
                Edge(
                    start.second,
                    minOf(start.first, end.first),
                    maxOf(start.first, end.first)
                )
            )
        }
    }
    return Pair(vertical, horizontal)
}

fun isRectangleIntersected(
    bounds: Rectangle,
    verticalEdges: List<Edge>,
    horizontalEdges: List<Edge>
): Boolean {
    for (edge in verticalEdges) {
        if (edge.c > bounds.minX && edge.c < bounds.maxX) {
            if (maxOf(edge.min, bounds.minY) < minOf(edge.max, bounds.maxY)) {
                return true
            }
        }
    }

    for (edge in horizontalEdges) {
        if (edge.c > bounds.minY && edge.c < bounds.maxY) {
            if (maxOf(edge.min, bounds.minX) < minOf(edge.max, bounds.maxX)) {
                return true
            }
        }
    }
    return false
}


fun isPointInside(point: Pair<Double, Double>, redTiles: List<Pair<Long, Long>>): Boolean {
    var _inside = false
    var n = redTiles.size

    for (i in redTiles.indices) {
        val start = redTiles[i]
        val end = redTiles[(i + 1) % redTiles.size]

        if ((start.second > point.second) != (end.second > point.second)) {
            val intersection = (end.first - start.first) * (point.second - start.second) / (end.second - start.second) + start.first
            if (point.first < intersection) {
                _inside = !_inside
            }
        }
    }
    return _inside
}




fun main() {
    val testInput = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent().split("\n")
    var input = testInput.map { line ->
        Pair<Long, Long>(
            line.split(",")[0].toLong(),
            line.split(",")[1].toLong()
        )}

    var filePath = "input.txt"
    input = File(filePath).readLines().map { line ->
        Pair<Long, Long>(
            line.split(",")[0].toLong(),
            line.split(",")[1].toLong()
        )}

    var redTiles = input

    var maximumArea = 0L
    for (tile in redTiles) {
        for (otherTile in redTiles) {
            if (tile == otherTile) {
                continue
            }
            val side1 = Math.abs(tile.first - otherTile.first) + 1
            val side2 = Math.abs(tile.second - otherTile.second) + 1
            val area = side1 * side2
            if (area > maximumArea) {
                maximumArea = area
            }
        }
    }
    println("Maximum simple area: $maximumArea")

    var filteredRedTiles = mutableListOf<Pair<Long, Long>>()
    for (i in 0 until redTiles.size) {
        val prev = redTiles[(i - 1 + redTiles.size) % redTiles.size]
        val curr = redTiles[i]
        val next = redTiles[(i + 1) % redTiles.size]

        if (!((prev.first == curr.first && curr.first == next.first) ||
              (prev.second == curr.second && curr.second == next.second))) {
            filteredRedTiles.add(curr)
        }
    }

    var maximumNewArea = 0L
    redTiles = filteredRedTiles

    val splitEdges = splitEdges(redTiles)
    val verticalEdges = splitEdges.first
    val horizontalEdges = splitEdges.second
    
    for (tile in redTiles) {
        for (otherTile in redTiles) {
            if (tile == otherTile) {
                continue
            }
            val minX = minOf(tile.first, otherTile.first)
            val maxX = maxOf(tile.first, otherTile.first)
            val minY = minOf(tile.second, otherTile.second)
            val maxY = maxOf(tile.second, otherTile.second)

            val width = maxX - minX + 1
            var height = maxY - minY + 1
            val area = width * height

            if (area <= maximumNewArea) {
                continue
            }

            if (isRectangleIntersected(
                Rectangle(minX, minY, maxX, maxY),
                verticalEdges,
                horizontalEdges
            )) {
                continue
            }

            val checkPoint = Pair(minX + 0.5, minY + 0.5)
            if (!(isPointInside(checkPoint, redTiles))) {
                println("Point $checkPoint is outside polygon")
                continue
            }
            
            println("New maximum area found: $area")
            maximumNewArea = area
        }
    }
    println("Maximum new area: $maximumNewArea")
}
