package days.day20

import utils.Direction
import utils.Field
import utils.Position
import utils.find
import utils.get
import utils.inputToDigitField
import utils.inputToField
import utils.isValidPosition
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.travel
import kotlin.math.abs

fun main() {
    shouldLog = true
//    val day = Day(20)
//    val year = Year(2024)
//    downloadInput(day, year)

    fun findShortestPath(start: Position, end: Position, f: Field): Pair<Array<Position>, Int> {
        val path = arrayOfNulls<Position>(10_000)
        var i = 0
        var prev: Position? = null
        var curr = start
        path[i++] = start
        while (curr != end) {
            var dir = Direction.UP
            while (true) {
                val target = curr.travel(dir)
                if (
                    f.isValidPosition(target) && f[target] != '#' && (prev != target)
                ) {
                    path[i++] = target
                    prev = curr
                    curr = target
                    break
                }
                dir = dir.turnRight()
            }
        }
        return path as Array<Position> to i
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

//    fun <K, V> Map<K, V>.countValues(): Map<V, Int> {
//        val valueCount = mutableMapOf<V, Int>()
//        for (value in values) {
//            valueCount[value] = valueCount.getOrDefault(value, 0) + 1
//        }
//        return valueCount
//    }
//
//    fun <K, V> Map<K, V>.inverseMap(): Map<V, List<K>> {
//        val inversedMap = mutableMapOf<V, MutableList<K>>()
//        for ((key, value) in this) {
//            inversedMap.computeIfAbsent(value) { mutableListOf() }.add(key)
//        }
//        return inversedMap
//    }

    fun shorterPathsWithInsaneTunneling(
        path: Array<Position>,
        pathSize: Int,
        minSkip: Int,
        maxTunnel: Int,
    ): Int {
        var amount = 0

        for (startI in 0 until pathSize) {
            val start = path[startI]
            for (endI in (startI + minSkip) until pathSize) {
                val end = path[endI]
                val positionsAdvanced = (endI - startI)
                val distance = start.distanceTo(end)
                val skipped = positionsAdvanced - distance
                if (distance <= maxTunnel && skipped >= minSkip) amount++
            }
        }

        return amount
    }

    fun part2(input: List<String>, minDiff: Int, maxTunnel: Int = 20): Int {
        val f = inputToField(input)
        val start = f.find { it == 'S' }
        val end = f.find { it == 'E' }

        val (path, pathSize) = findShortestPath(start, end, f)

        return shorterPathsWithInsaneTunneling(
            path = path,
            minSkip = minDiff,
            maxTunnel = maxTunnel,
            pathSize = pathSize
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
//        .also {
//            submit(it, day, year, Part(2))
//        }
        .println()
}
