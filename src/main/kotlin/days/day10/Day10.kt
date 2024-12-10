package days.day10

import utils.DigitField
import utils.Direction
import utils.Position
import utils.findAll
import utils.get
import utils.inputToDigitField
import utils.isValidPosition
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import utils.travel

fun main() {
    fun search(pos: Position, f: DigitField): List<Position> {
        val value = f[pos]
        if (shouldLog) println("At $pos ($value)")
        if (value == 9) return listOf(pos)
        return listOf(
            pos.travel(Direction.UP),
            pos.travel(Direction.DOWN),
            pos.travel(Direction.LEFT),
            pos.travel(Direction.RIGHT),
        )
            .filter { f.isValidPosition(it) && f[it] == value + 1 }
            .map { search(it, f) }
            .flatten()

    }

    fun part1(input: List<String>): Int {
        val f = inputToDigitField(input)
        val starts = f.findAll { it == 0 }
        return starts.sumOf { start ->
            search(start, f)
                .toSet()
                .also { if (shouldLog) println(it) }
                .size
        }
    }

    fun part2(input: List<String>): Int {
        val f = inputToDigitField(input)
        val trailheads = f.findAll { it == 0 }
        return trailheads.sumOf { start ->
            search(start, f)
                .also { found -> if (shouldLog) println(found) }
                .size
        }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day10_test",
        {
            part1(it)
        },
        1,
        filterBlank = false,
    )
    shouldLog = false
    val input = readInput("Day10").filter(String::isNotBlank)
    part1(input).println()

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day10_test2",
        ::part2,
        81,
        filterBlank = false,
    )
    shouldLog = false
    val input2 = readInput("Day10").filter(String::isNotBlank)
    part2(input2).println()
}
