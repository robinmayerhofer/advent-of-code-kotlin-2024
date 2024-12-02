package days.day02

import utils.*
import kotlin.math.abs

fun main() {
    shouldLog = true

    fun List<Int>.allIncreasing(): Boolean =
        zipWithNext().all { it.first < it.second }

    fun List<Int>.allDecreasing() : Boolean =
        zipWithNext().all { it.first > it.second }

    fun List<Int>.maxDiffEqualOrBelow(value: Int): Boolean =
        zipWithNext().all { abs(it.first - it.second) <= value }

    fun part1(input: List<String>): Int =
        input.count {
            val numbers = it.findAllNumbers()
            (numbers.allDecreasing() || numbers.allIncreasing()) && numbers.maxDiffEqualOrBelow(3)
        }

    fun part2(input: List<String>): Int =
        input.count {
            val numbers = it.findAllNumbers()
            numbers.indices.any { index ->
                val sublist = numbers.toMutableList().apply { removeAt(index) }
                (sublist.allDecreasing() || sublist.allIncreasing()) && sublist.maxDiffEqualOrBelow(3)
            }
        }

    println("Part 1 Test")
    testFile(
        "Part 1 Test 1",
        "Day02_test",
        {
            part1(it)
        },
        2,
        filterBlank = false,
    )
    println("Part 1")
    val input = readInput("Day02").filter(String::isNotBlank)
    part1(input).println()

    println("Part 2 Test")
    testFile(
        "Part 2 Test 1",
        "Day02_test",
        ::part2,
        4,
        filterBlank = false,
    )
    println("Part 2")
    val input2 = readInput("Day02").filter(String::isNotBlank)
    part2(input2).println()
}
