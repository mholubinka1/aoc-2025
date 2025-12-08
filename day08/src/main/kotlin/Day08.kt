import java.io.File

data class JunctionBox (
    var id: Int, 
    val x: Long,
    val y: Long,
    val z: Long
)

data class JunctionBoxPair (
    val a: JunctionBox,
    val b: JunctionBox, 
    val distance: Float
)

fun distance(a: JunctionBox, b: JunctionBox): Float {
    return kotlin.math.sqrt(
        (((a.x - b.x) * (a.x - b.x)).toFloat()   +
        ((a.y - b.y) * (a.y - b.y)).toFloat()   +
        ((a.z - b.z) * (a.z - b.z)).toFloat())
    )
}

fun parse_input(lines: List<String>): List<JunctionBox> {
    var junctionBoxes: MutableList<JunctionBox> = mutableListOf<JunctionBox>()

    for (line in lines) {
        var parts = line.split(",")
        junctionBoxes.add(JunctionBox(
            id = junctionBoxes.size,
            x = parts[0].toLong(),
            y = parts[1].toLong(),
            z = parts[2].toLong()
        ))
    }    
    
    return junctionBoxes
}


fun main() {
    val testInput = """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent().split("\n")
    var input = parse_input(testInput)

    val filePath = "input.txt"
    input = parse_input(File(filePath).readLines())

    var circuits: MutableList<MutableSet<Int>> = mutableListOf<MutableSet<Int>>()

    val pairsToFind = 1000
    var pairs: MutableList<JunctionBoxPair> = mutableListOf<JunctionBoxPair>()

    for (i in 0 until input.size) {
        for (j in i + 1 until input.size) {
            val d = distance(input[i], input[j])
            pairs.add(JunctionBoxPair(
                a = input[i],
                b = input[j],
                distance = d
            ))
        }
    }

    var closest = pairs.sortedBy { it.distance } //.take(pairsToFind)
    for (pair in closest) {
        var matchingCircuits = circuits.filter { it.contains(pair.a.id) || it.contains(pair.b.id) }
        when (matchingCircuits.size) {
            0 -> { 
                circuits.add(mutableSetOf(pair.a.id, pair.b.id))
                println("New circuit with ${pair.a.id} and ${pair.b.id}")
            } 
            1 -> {
                matchingCircuits[0].add(pair.a.id)
                matchingCircuits[0].add(pair.b.id)
                println("Existing circuit: ${matchingCircuits[0]}")
            } 
            else -> {
                val c1 = matchingCircuits[0]
                val c2 = matchingCircuits[1]
                println("Merged circuits: $c1")
                c1.addAll(c2)
                circuits.remove(c2)          
            }
        }
        if (circuits.size == 1) {
            if (circuits[0].size == input.size) {
                val result = pair.a.x * pair.b.x
                println("All junction boxes connected. Result: $result")
                break
            }
        }  
    }

    /*var result = 1L
    for (circuit in circuits.sortedByDescending { it.size }.take(3)) {
        result *= circuit.size.toLong()
    }
    println("Result: $result")*
}
