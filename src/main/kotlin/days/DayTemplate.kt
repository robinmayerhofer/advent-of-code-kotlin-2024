package days

import utils.measure
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

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day01_test",
        {
            part1(it)
        },
        2,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day01").filter(String::isNotBlank)
        part1(input)
    }.println()

//    shouldLog = true
//    testFile(
//        "Part 2 Test 1",
//        "Day01_test",
//        ::part2,
//        1,
//        filterBlank = false,
//    )
//    shouldLog = false
//    println("Solving part 2")
//    measure {
//        val input = readInput("Day01").filter(String::isNotBlank)
//        part2(input)
//    }.println()
}
