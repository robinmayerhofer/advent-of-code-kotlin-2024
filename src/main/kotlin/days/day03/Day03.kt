package days.day03

import days.day03.command.CommandFactory
import days.day03.command.CommandInterpreter
import utils.*


fun main() {
    shouldLog = true

    fun part1(input: List<String>): Int =
        CommandFactory.parse(input)
            .let { CommandInterpreter(it).execute() }

    fun part2(input: List<String>): Int =
        CommandFactory.parse(input)
            .let { CommandInterpreter(it).execute() }

    testFile(
        "Part 1 Test 1",
        "Day03_test",
        {
            part1(it)
        },
        161,
        filterBlank = false,
    )
    val input = readInput("Day03")
    part1(input).println()

    testFile(
        "Part 2 Test 1",
        "Day03_test2",
        ::part2,
        48,
        filterBlank = false,
    )
    val input2 = readInput("Day03").filter(String::isNotBlank)
    part2(input2).println()
}
