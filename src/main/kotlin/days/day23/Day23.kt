package days.day23

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import days.day23.Day23.find3FullyConnected
import days.day23.Day23.findLongestMaxCliqueBronKerbosch
import days.day23.Day23.findMaxCliquesBronKerbosch
import days.day23.Day23.parse
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

object Day23 {

    lateinit var undirectedEdges: List<Pair<String, String>>
    lateinit var vertices: Array<String>
    lateinit var vertexToEdges: MutableMap<String, MutableList<String>>
    lateinit var partitions: List<List<String>>
    lateinit var vertexToIndex: Map<String, Int>

    fun computeVertexToEdgesMap(undirectedEdges: List<Pair<String, String>>): MutableMap<String, MutableList<String>> =
        undirectedEdges.fold(mutableMapOf()) { acc, edge ->
            acc.putIfAbsent(edge.first, mutableListOf())
            acc.putIfAbsent(edge.second, mutableListOf())
            acc[edge.first]!!.add(edge.second)
            acc[edge.second]!!.add(edge.first)
            acc
        }

    fun areFullyConnected(list: List<String>): Boolean {
        // check first to each, second to all but first because of undirected edges, and so on. Generically for lists of any size
        return list.all { vertex ->
            list.filter { it != vertex }.all { other ->
                other in vertexToEdges[vertex]!!
            }
        }
    }

    fun parse(input: List<String>) {
        undirectedEdges = input
            .filter { it.isNotBlank() }
            .map { it.split("-", limit = 2) }
            .map { (a, b) -> a to b }
        vertices = undirectedEdges.flatMap { listOf(it.first, it.second) }.toSet()
            .sorted()
            .toTypedArray()
        vertexToEdges = computeVertexToEdgesMap(undirectedEdges)
        vertexToIndex = vertices.mapIndexed { index, vertex -> vertex to index }.toMap()
    }

    fun find3FullyConnected(): Int =
        vertices.indices.sumOf { i ->
            (i+1 until vertices.size).sumOf { j ->
                (j+1 until vertices.size).count { k ->
                    val a = vertices[i]
                    val b = vertices[j]
                    val c = vertices[k]
                    ('t' == a[0] || 't' == b[0] || 't' == c[0]) && areFullyConnected(listOf(vertices[i], vertices[j], vertices[k]))
                }
            }
        }

    private fun findMaxCliquesBronKerbosch(vertexIndices: List<Int>): List<List<Int>> {
        if (vertexIndices.lastOrNull() == vertexIndices.size - 1) {
            return listOf(vertexIndices)
        }

        if (vertexIndices.isEmpty()) {
            return vertices.indices.map {
                findMaxCliquesBronKerbosch(listOf(it))
            }
                .also { if (shouldLog) println(it.joinToString("\n") { "${it.size} to $it" }) }
                .maxBy { it.size }
        }

        val currentVertex = vertices[vertexIndices.last()]
        val nextNeighborsIndices: List<Int> = vertexToEdges[currentVertex]!!
            .map { vertexToIndex[it]!! }
            .filter { it > vertexIndices.last() }
            .filter { newIndex ->
                vertexIndices.all { vertexIndex ->
                    vertices[newIndex] in vertexToEdges[vertices[vertexIndex]]!!
                }
            }
            .sorted()
            .takeIf { it.isNotEmpty() } ?: return listOf(vertexIndices)

        // somehow it works without checking all the neighbors
        return nextNeighborsIndices.map { nextNeighborIndex ->
            findMaxCliquesBronKerbosch(vertexIndices + nextNeighborIndex)
        }.flatten()
    }

    // A max clique is a clique that cannot be extended by adding another vertex.
    // This attempts to find a max clique by starting with each vertex and checking if it can be extended.
    // We only extend with the next neighbor in alphabetical order.
    // Thus, we find a single max clique, but not necessarily the largest one.
    fun findLongestMaxCliqueBronKerbosch(): List<String> {
        val maxCliques = findMaxCliquesBronKerbosch(emptyList())
        return maxCliques
            .also { println("Found ${it.size} max cliques") }
            .maxBy { it.size } // the first one will be the alphabetically first one because the algorithm operates on sorted vertices
            .map { vertices[it] }
    }
}

fun main() {
    shouldLog = true
    val day = Day(23)
    val year = Year(2024)
    downloadInput(day, year)

    fun part1(input: List<String>): Int {
        parse(input)
        return find3FullyConnected()
    }

    fun part2(input: List<String>): String {
        parse(input)
        return findLongestMaxCliqueBronKerbosch().sorted().joinToString(",")
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day23_test",
        {
            part1(it)
        },
        7,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day23").filter(String::isNotBlank)
        part1(input)
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day23_test",
        ::part2,
        "co,de,ka,ta",
        filterBlank = false,
    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day23").filter(String::isNotBlank)
        part2(input)
    }
        .also {
            submit(it, day, year, Part(2))
        }
        .println()
}
