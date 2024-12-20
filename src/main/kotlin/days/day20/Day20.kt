package days.day20

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.Direction
import utils.Field
import utils.Position
import utils.find
import utils.get
import utils.inputToField
import utils.isValidPosition
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import utils.travel
import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    shouldLog = true
    val day = Day(20)
    val year = Year(2024)
    downloadInput(day, year)

    class Vertex(
        val pos: Position,
        val cost: Int,
        val tunnelStart: Position? = null,
        val tunnelEnd: Position? = null,
        val prev: Vertex?,
    ) : Comparable<Vertex> {
        override fun compareTo(other: Vertex): Int =
            cost - other.cost

        override fun toString(): String {
            return "Vertex(pos=$pos, cost=$cost, tunnelStart=$tunnelStart, tunnelEnd=$tunnelEnd)"
        }
    }

    fun extractPath(vertex: Vertex): List<Position> = buildList {
        var v: Vertex? = vertex
        while (v != null) {
            add(v.pos)
            v = v.prev
        }
    }.reversed()

    fun findShortestPath(start: Position, end: Position, f: Field): List<Position> {
        val q = PriorityQueue<Vertex>()
        q.add(Vertex(start, 0, prev = null))

        val dist = mutableMapOf<Position, Int>()

        while (q.isNotEmpty()) {
            val v = q.poll()
            if (dist[v.pos] != null) continue
            if (v.pos == end) return extractPath(v)
            dist[v.pos] = v.cost
            q.addAll(
                listOf(
                    Vertex(v.pos.travel(Direction.UP), v.cost + 1, prev = v),
                    Vertex(v.pos.travel(Direction.DOWN), v.cost + 1, prev = v),
                    Vertex(v.pos.travel(Direction.LEFT), v.cost + 1, prev = v),
                    Vertex(v.pos.travel(Direction.RIGHT), v.cost + 1, prev = v),
                )
                    .filter { f.isValidPosition(it.pos) }
                    .filter { f[it.pos] != '#' }
                    .filter { dist[it.pos] == null }
            )
        }
        error("Not found")
    }

//    fun shorterPathsWithTunneling(
//        f: Field,
//        path: Map<Position, Int>,
//        minSkip: Int,
//    ): Int {
//        val possibleTunnels: Sequence<Pair<Position, Position>> = sequence {
//            path.keys.asSequence().forEach { start ->
//                listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).forEach { dir ->
//                    val middle = start.travel(dir)
//                    if (f[middle] == '#') {
//                        listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).forEach { dir ->
//                            val end = middle.travel(dir)
//                            if (f.isValidPosition(end) && f[end] != '#') {
//                                yield(start to end)
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
//
//        return possibleTunnels.count { (start, end) ->
//            val tunnelStartPosition = path[start]!!
//            val tunnelEndPosition = path[end]!!
//            val skipped = tunnelEndPosition - tunnelStartPosition - 2
//            println("$tunnelStartPosition to $tunnelEndPosition skips $skipped")
//            skipped >= minSkip
//        }
//    }

    fun Position.distanceTo(other: Position) = abs(row - other.row) + abs(column - other.column)

    fun <K, V> Map<K, V>.countValues(): Map<V, Int> {
        val valueCount = mutableMapOf<V, Int>()
        for (value in values) {
            valueCount[value] = valueCount.getOrDefault(value, 0) + 1
        }
        return valueCount
    }

    fun <K, V> Map<K, V>.inverseMap(): Map<V, List<K>> {
        val inversedMap = mutableMapOf<V, MutableList<K>>()
        for ((key, value) in this) {
            inversedMap.computeIfAbsent(value) { mutableListOf() }.add(key)
        }
        return inversedMap
    }

    fun shorterPathsWithInsaneTunneling(
        path: List<Position>,
        minSkip: Int,
    ): Int {
        var amount = 0

        for (startI in path.indices) {
            val start = path[startI]
            for (endI in (startI + minSkip) until path.size) {
                val end = path[endI]
                val positionsAdvanced = (endI - startI)
                val distance = start.distanceTo(end)
                val skipped = positionsAdvanced - distance
                if (distance <= 20 && skipped >= minSkip) amount++
            }
        }

        return amount
    }

    fun part2(input: List<String>, minDiff: Int): Int {
        val f = inputToField(input)
        val start = f.find { it == 'S' }
        val end = f.find { it == 'E' }

        val shortestPath = findShortestPath(start, end, f)

        val shortestPathCost = shortestPath.size - 1
        println("shortestPathCost: $shortestPathCost")

        return shorterPathsWithInsaneTunneling(
            path = shortestPath,
            minSkip = minDiff,
        )
    }

//    shouldLog = true
//    testFile(
//        "Part 1 Test 1",
//        "Day20_test",
//        {
//            part1(it, minDiff = 1)
//        },
//        44,
//        filterBlank = false,
//    )
//
//    shouldLog = false
//    println("Solving part 1")
//    measure {
//        val input = readInput("Day20")
//        part1(input, minDiff = 100)
//    }
//        .also {
//            submit(it, day, year, Part(1))
//        }
//        .println()

//    shouldLog = true
//    testFile(
//        "Part 2 Test 1",
//        "Day20_test",
//        {
//            part2(it, minDiff = 50)
//        },
//        285,
//        filterBlank = false,
//    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day20")
        part2(input, minDiff = 100)
    }
        .also {
            submit(it, day, year, Part(2))
        }
        .println()
}
