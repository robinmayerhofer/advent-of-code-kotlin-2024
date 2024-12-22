package days.day22

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.findAllNumbersLong
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true
    val day = Day(22)
    val year = Year(2024)
    downloadInput(day, year)

    fun mix(a: Long, b: Long) = a xor b
    fun prune(a: Long, b: Long = 16777216) = a % b

    fun Long.next(): Long {
        val step1 = prune(mix(this, this * 64))
        val step2 = prune(mix(step1, step1 / 32))
        val step3 = prune(mix(step2, step2 * 2048))
        return step3
    }

    fun Long.step(count: Int): Long {
        var result = this
        repeat(count) {
            result = result.next()
        }
        return result
    }

    check(mix(42, 15) == 37L)
    check(prune(100000000) == 16113920L)
    check(123L.next() == 15887950L)
    check(123L.next().next() == 16495136L)
    check(1L.step(2000) == 8685429L)

    fun part1(input: List<String>): Long =
        input
            .filter { it.isNotBlank() }
            .map { it.findAllNumbersLong()[0] }
            .sumOf {
                it.step(2000)
            }

    fun part2(input: List<String>): Int {
        val secretCount = 2000
        val numberss: List<List<Int>> = input
            .filter { it.isNotBlank() }
            .map { it.findAllNumbersLong()[0] }
            .map {
                generateSequence(it) { prev -> prev.next() }
                    .take(secretCount + 1)
                    .map { (it % 10).toInt() }
                    .toList()
            }

        val diffs = numberss.map { it.zipWithNext { a, b -> b - a } }

        val final = Array(19) { Array(19) { Array(19) { IntArray(19) } } }

        for (i in input.indices) {
            val cur = Array(19) { Array(19) { Array(19) { IntArray(19) } } }
            for (j in 1996 downTo 0) {
                cur
                    .get(diffs[i][j] + 9)
                    .get(diffs[i][j + 1] + 9)
                    .get(diffs[i][j + 2] + 9)
                    .set(diffs[i][j + 3] + 9, numberss[i][j + 4])
            }
            for (A in 0 until 19) {
                for (B in 0 until 19) {
                    for (C in 0 until 19) {
                        for (D in 0 until 19) {
                            final[A][B][C][D] += cur[A][B][C][D]
                        }
                    }
                }
            }
        }

        var ans = 0
        for (A in 0 until 19) {
            for (B in 0 until 19) {
                for (C in 0 until 19) {
                    for (D in 0 until 19) {
                        ans = maxOf(ans, final[A][B][C][D])
                    }
                }
            }
        }

        return ans
    }

    shouldLog = false
    testFile(
        "Part 1 Test 1",
        "Day22_test",
        {
            part1(it)
        },
        37327623,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day22").filter(String::isNotBlank)
        part1(input)
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()

    shouldLog = false
    testFile(
        "Part 2 Test 1",
        "Day22_test2",
        ::part2,
        23,
        filterBlank = false,
    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day22")
        part2(input)
    }
        .also {
            submit(it, day, year, Part(2))
        }
        .println()
}
