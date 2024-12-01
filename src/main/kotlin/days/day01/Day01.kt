package days.day01

import utils.findAllNumbers
import utils.println
import utils.readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val data = input.map { it.findAllNumbers() }

        val n1 = data.map { it[0] }.sorted()
        val n2 = data.map { it[1] }.sorted()
        return n1.zip(n2).sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val data = input.map { it.findAllNumbers() }

        val n1 = data.map { it[0] }
        val countMap: Map<Int, Int> = data.map { it[1] }.groupingBy { it }.eachCount()

        return n1.sumOf { number ->
            number * countMap.getOrDefault(number, 0)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    val input = readInput("Day01")
    part1(input).println()

    // test if implementation meets criteria from the description, like:
    check(part2(testInput) == 31)
    part2(input).println()
}
