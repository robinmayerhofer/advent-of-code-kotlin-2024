package days.day12

import utils.Field
import utils.Position
import utils.area
import utils.edges
import utils.floodFill
import utils.inputToField
import utils.measure
import utils.perimeter
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true

    fun findAreas(f: Field): List<Set<Position>> {
        val areas = mutableListOf<Set<Position>>()
        for ((rowI, row) in f.withIndex()) {
            for (columnI in row.indices) {
                val position = Position(column = columnI, row = rowI)
                if (areas.any { position in it }) {
                    continue
                }
                areas.add(
                    f.floodFill(position)
                )
            }
        }

        return areas
    }

    fun part1(input: List<String>): Int {
        val f = inputToField(input)
        val areas = findAreas(f)
        return areas.sumOf { it.area() * it.perimeter() }
    }

    fun part2(input: List<String>): Int {
        val f = inputToField(input)
        val areas = findAreas(f)
        return areas.sumOf { it.area() * it.edges() }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day12_test",
        {
            part1(it)
        },
        1930,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day12").filter(String::isNotBlank)
        part1(input)
    }.println()

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day12_test",
        ::part2,
        1206,
        filterBlank = false,
    )

    testFile(
        "Part 2 Test 1",
        "Day12_test2",
        ::part2,
        80,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day12").filter(String::isNotBlank)
        part2(input)
    }
        .println()
}
