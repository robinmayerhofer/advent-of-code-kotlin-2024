package days.day02

import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true

    fun part1(input: List<String>): Int =
        input.size

    fun part2(input: List<String>): Int =
        input.size

    testFile(
        "Part 1 Test 1",
        "Day02_test",
        {
            part1(it)
        },
        2,
        filterBlank = false,
    )
    val input = readInput("Day24").filter(String::isNotBlank)
    part1(input).println()

//    testFile(
//        "Part 2 Test 1",
//        "Day02_test",
//        ::part2,
//        1,
//        filterBlank = false,
//    )
//    val input2 = readInput("Day02").filter(String::isNotBlank)
//    part2(input2).println()
}
