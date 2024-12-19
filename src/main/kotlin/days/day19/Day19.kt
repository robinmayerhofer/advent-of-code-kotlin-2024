package days.day19

import client.Day
import client.Download
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import days.day01.part2
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true
    val day = Day(19)
    val year = Year(2024)
    downloadInput(day, year)

    fun isPossible(towels: List<String>, pattern: String, foundPattern: String): Boolean {
        if (pattern == foundPattern) return true
        return towels
            .map { foundPattern + it }
            .filter { newPattern -> pattern.startsWith(newPattern) }
            .any { newPattern -> isPossible(towels, pattern, newPattern) }
    }

    fun part1(input: List<String>): Int {
        val towels = input[0].split(",").map { it.trim() }

        return input.drop(2).count { pattern ->
            isPossible(towels, pattern, foundPattern = "").also {
                if (shouldLog) { println("$pattern $it") }
            }
        }
    }

    val memory: MutableMap<Pair<String, String>, Long> = mutableMapOf()

    fun possibleWays(towels: List<String>, pattern: String, foundPattern: String): Long {
        if (pattern == foundPattern) return 1
        memory[pattern to foundPattern]?.let { return it }
        return towels
            .map { foundPattern + it }
            .filter { newPattern -> pattern.startsWith(newPattern) }
            .sumOf { newPattern -> possibleWays(towels, pattern, newPattern) }
            .also { memory[pattern to foundPattern] = it }
    }

    fun part2(input: List<String>): Long {
        memory.clear()
        val towels = input[0].split(",").map { it.trim() }

        return input.drop(2).sumOf { pattern ->
            println(pattern)
            possibleWays(towels, pattern, foundPattern = "").also {
                println("$pattern $it")
            }
        }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day19_test",
        {
            part1(it)
        },
        6,
        filterBlank = false,
    )

    shouldLog = true
    println("Solving part 1")
    measure {
        val input = readInput("Day19")
        part1(input)
    }
        .also { submit(it, day, year, Part(1)) }
        .println()

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day19_test",
        ::part2,
        16,
        filterBlank = false,
    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day19")
        part2(input)
    }
        .also {submit(it, day, year, Part(2)) }
        .println()
}
