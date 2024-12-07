package days.day07

import utils.findAllNumbersLong
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import kotlin.math.pow

fun main() {
    shouldLog = true

    fun calculate(target: Long, current: Long, numbers: List<Long>): Boolean {
        if (numbers.isEmpty()) {
            return target == current
        }
        val newNs = numbers.drop(1)
        return calculate(target, current + numbers[0], newNs) ||
                calculate(target, current * numbers[0], newNs)
    }

    fun canBeTrue(line: String): Boolean {
        val numbers = line.findAllNumbersLong()
        if (numbers.isEmpty()) { return false }
        return calculate(target = numbers[0], current = numbers[1], numbers = numbers.drop(2))
    }

    fun part1(input: List<String>): Long =
        input.filter { canBeTrue(it) }
            .sumOf { it.findAllNumbersLong()[0] }

    fun concat(a: Long, b: Long): Long {
        val l = b.toString().length
        return (a * 10.0.pow(l) + b).toLong()
    }

    fun calculate2(target: Long, current: Long, numbers: List<Long>): Boolean {
        if (numbers.isEmpty()) {
            return target == current
        }
        val newNs = numbers.drop(1)
        return calculate2(target, current + numbers[0], newNs) ||
                calculate2(target, current * numbers[0], newNs) ||
                calculate2(target, concat(current, numbers[0]), newNs)
    }

    fun canBeTrue2(line: String): Boolean {
        val numbers = line.findAllNumbersLong()
        if (numbers.isEmpty()) { return false }
        return calculate2(target = numbers[0], current = numbers[1], numbers = numbers.drop(2)).also {
            if (shouldLog) println("$it $line")
        }
    }


    fun part2(input: List<String>): Long =
        input.filter { canBeTrue2(it) }
            .sumOf { it.findAllNumbersLong()[0] }

    testFile(
        "Part 1 Test 1",
        "Day07_test",
        {
            part1(it)
        },
        3749,
        filterBlank = false,
    )
    val input = readInput("Day07").filter(String::isNotBlank)
    part1(input).println()

    testFile(
        "Part 2 Test 1",
        "Day07_test",
        ::part2,
        11387,
        filterBlank = false,
    )
    measure {
        val input2 = readInput("Day07").filter(String::isNotBlank)
        part2(input2).println()
    }
}
