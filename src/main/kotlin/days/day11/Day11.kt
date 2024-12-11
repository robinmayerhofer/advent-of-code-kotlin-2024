package days.day11

import utils.findAllNumbersLong
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true

    fun Long.hasEvenDigitCount(): Boolean = "$this".length % 2 == 0

    fun Long.split(): Sequence<Long> {
        val s = "$this"
        val l = s.length
        return sequenceOf(
            s.substring(0, l / 2).toLong(),
            s.substring(l / 2, l).toLong(),
        )
    }

    val stonesToTimesToOutcome: MutableMap<Long, MutableMap<Int, Long>> = mutableMapOf()

    fun blink(stone: Long, times: Int): Long {
        if (times == 0) {
            return 1
        }

        stonesToTimesToOutcome[stone]?.get(times)?.let { return it }

        val outcome = when {
            stone == 0L -> blink(1, times - 1)
            stone.hasEvenDigitCount() -> stone.split().sumOf { blink(it, times - 1) }
            else -> blink(stone * 2024L, times - 1)
        }

        stonesToTimesToOutcome.getOrPut(stone) { mutableMapOf() }[times] = outcome
        return outcome
    }

    fun part1(input: List<String>): Long {
        val stones = input[0].findAllNumbersLong()
        return stones.sumOf { stone ->
            blink(stone, times = 25)
        }
    }


    fun part2(input: List<String>): Long {
        val stones = input[0].findAllNumbersLong()
        return stones.sumOf { stone ->
            blink(stone, times = 75)
        }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day11_test",
        {
            part1(it)
        },
        55312,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day11").filter(String::isNotBlank)
        part1(input)
    }.println()

    shouldLog = false
    println("Solving part 2")
    measure {
        val input2 = readInput("Day11").filter(String::isNotBlank)
        part2(input2)
    }.println()
}
