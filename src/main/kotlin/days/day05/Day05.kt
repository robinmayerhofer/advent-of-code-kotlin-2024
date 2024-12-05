package days.day05

import utils.findAllNumbers
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = false

    fun transformInput(input: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val (rules, updates) = input
            .filterNot { it.isBlank() }
            .partition { it.contains("|") }

        val rulesMap = rules
            .map { it.findAllNumbers() }
            .groupBy({ it[0] }, { it[1] })

        val updatesList = updates.map { it.findAllNumbers() }

        return rulesMap to updatesList
    }

    fun checkUpdate(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        return update.withIndex().none { (index, updateNumber) ->
            val restOfList = update.subList(index + 1, update.size)
            restOfList.any { numberAfterward ->
                rules[numberAfterward]?.contains(updateNumber) == true
            }
        }
    }

    fun List<Int>.middleNumber(): Int {
        assert(size % 2 == 1)
        return this[size / 2]
    }

    fun part1(input: List<String>): Int {
        val (rules, updates) = transformInput(input)
        return updates.filter { checkUpdate(it, rules) }.sumOf { it.middleNumber() }
    }

    tailrec fun fixUpdate(update: List<Int>, rules: Map<Int, List<Int>>): List<Int> {
        for ((index, updateNumber) in update.withIndex()) {
            for (numberAfterward in update.subList(index + 1, update.size)) {
                if (rules[numberAfterward]?.contains(updateNumber) == true) {
                    return fixUpdate(
                        update = update.toMutableList().apply {
                            remove(numberAfterward)
                            add(index = index, element = numberAfterward)
                        },
                        rules = rules
                    )
                }
            }
        }
        return update
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = transformInput(input)

        return updates
            .filterNot { checkUpdate(it, rules) }
            .map { fixUpdate(it, rules) }
            .sumOf { it.middleNumber() }
    }

    testFile(
        "Part 1 Test 1",
        "Day05_test",
        ::part1,
        143,
        filterBlank = false,
    )

    val input = readInput("Day05").filter(String::isNotBlank)
    part1(input)
        .println()

    testFile(
        "Part 2 Test 1",
        "Day05_test",
        ::part2,
        123,
        filterBlank = false,
    )
    val input2 = readInput("Day05").filter(String::isNotBlank)
    part2(input2).println()
}
