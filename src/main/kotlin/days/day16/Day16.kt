package days.day16

import client.Day
import client.Download
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true
    val day = Day(16)
    val year = Year(2024)
    downloadInput(day, year)

    fun part1(input: List<String>): Int =
        input.size

    fun part2(input: List<String>): Int =
        input.size

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day16_test",
        {
            part1(it)
        },
        2,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day16").filter(String::isNotBlank)
        part1(input)
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()

//    shouldLog = true
//    testFile(
//        "Part 2 Test 1",
//        "Day16_test",
//        ::part2,
//        1,
//        filterBlank = false,
//    )
//    shouldLog = false
//    println("Solving part 2")
//    measure {
//        val input = readInput("Day16").filter(String::isNotBlank)
//        part2(input)
//    }
//        .also {
//            submit(it, day, year, Part(2))
//        }
//        .println()
}
