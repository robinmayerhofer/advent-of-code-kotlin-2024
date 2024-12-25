package days.day25

import client.Day
import client.Download
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.Field
import utils.get
import utils.inputToField
import utils.measure
import utils.println
import utils.readInput
import utils.rotate90
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true
    val day = Day(25)
    val year = Year(2024)
    downloadInput(day, year)

    fun Field.charCountsPerColumn(char: Char): List<Int> =
        rotate90(this)
            .map { line -> line.count { it == char } }

    fun part1(input: List<String>): Int {
        val keysAndLocks: List<Field> = input
            .joinToString("\n")
            .split("\n\n")
            .map { it.split("\n") }
            .map { inputToField(it) }

        val locksHeights = keysAndLocks
            .filter { it.get(0, 0) == '#' }
            .map { it.charCountsPerColumn('#') }

        val keysHeights = keysAndLocks
            .filter { it.get(0, 0) == '.' }
            .map { it.charCountsPerColumn('#') }

        return locksHeights.sumOf { lockHeights ->
            keysHeights.count { keyHeights ->
                keyHeights.zip(lockHeights).all { (k, l) -> k + l <= 7 }
            }
        }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day25_test",
        {
            part1(it)
        },
        3,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day25")
        part1(input)
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()
}
