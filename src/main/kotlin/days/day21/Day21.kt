package days.day21

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.Field
import utils.Position
import utils.inputToField
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import kotlin.math.max
import kotlin.math.min

typealias Keyboard = Map<Char, Position>

class Calculator(private val code: String, private val maxLayer: Int) {
    companion object {
        private fun Field.charsToPositions(): Map<Char, Position> =
            mapIndexed { row, line ->
                line.mapIndexed { column, char ->
                    char to Position.of(row, column)
                }
            }.flatten().toMap()

        private val numericKeyboard: Keyboard = inputToField(
            listOf(
                "789",
                "456",
                "123",
                "-0A",
            )
        ).charsToPositions()

        private val directionalKeyboard: Keyboard = inputToField(
            listOf(
                "-^A",
                "<v>",
            )
        ).charsToPositions()
    }

    private val keyboards: Array<Keyboard> = Array(maxLayer) {
        if (it == 0) numericKeyboard
        else directionalKeyboard
    }

    private fun position(layer: Int, code: Char): Position = keyboards[layer][code]!!

    private fun repeatChar(char: Char, times: Int) = char.toString().repeat(times)

    private val cache: MutableMap<Pair<Int, String>, Long> = mutableMapOf()

    private fun calculateDistance(layer: Int, code: String): Long {
        if (layer == maxLayer) {
            // just type it in :D
            return code.length.toLong()
        }
        cache[layer to code]?.let { return it }

        var totalMovement = 0L
        var state = 'A'

        for (codeChar in code) {
            val start = position(layer, state)
            val target = position(layer, codeChar)
            val nope = position(layer, '-')
            var minMovement: Long = Long.MAX_VALUE

            val xDiff = start.x - target.x
            val yDiff = start.y - target.y

            val verticalSteps = repeatChar('^', max(xDiff, 0)) + repeatChar('v', max(-xDiff, 0))
            val horizontalSteps = repeatChar('<', max(yDiff, 0)) + repeatChar('>', max(-yDiff, 0))

            if (target.x != nope.x || start.y != nope.y) {
                // Will not hit no-go zone when first moving vertically then horizontally.
                minMovement = min(
                    minMovement,
                    calculateDistance(layer + 1, verticalSteps + horizontalSteps + 'A')
                )
            }
            if (start.x != nope.x || target.y != nope.y) {
                // Will not hit no-go zone when first moving horizontally then vertically.
                minMovement = min(
                    minMovement,
                    calculateDistance(layer + 1, horizontalSteps + verticalSteps + 'A')
                )
            }
            check(minMovement != Long.MAX_VALUE) { "We should always be able to move to the target." }
            totalMovement += minMovement
            state = codeChar
        }
        cache[layer to code] = totalMovement
        return totalMovement
    }

    fun calculate(): Long =
        calculateDistance(0, code) * code.substring(0, code.length - 1).toLong()
}

fun main() {
    shouldLog = true
    val day = Day(21)
    val year = Year(2024)
    downloadInput(day, year)

    fun part1(input: List<String>): Long =
        input.sumOf { exp ->
            Calculator(exp, maxLayer = 3).calculate()
        }

    fun part2(input: List<String>): Long =
        input.sumOf { exp ->
            Calculator(exp, maxLayer = 26).calculate()
        }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day21_test",
        {
            part1(it)
        },
        126384,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day21").filter(String::isNotBlank)
        part1(input)
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()

    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day21").filter(String::isNotBlank)
        part2(input)
    }
        .also {
            submit(it, day, year, Part(2))
        }
        .println()
}
