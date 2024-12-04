package days.day04

import utils.*
import utils.Direction.*

fun main() {
    shouldLog = true

    fun findPart1(field: Field, startPosition: Position, direction: Direction): Boolean =
        field.getSafe(startPosition.travel(direction, steps = 1)) == 'M' &&
                field.getSafe(startPosition.travel(direction, steps = 2)) == 'A' &&
                field.getSafe(startPosition.travel(direction, steps = 3)) == 'S'

    fun part1(input: List<String>): Int {
        val field = inputToField(input)
        val startPositions = field.findAll { it == 'X' }
        return startPositions.sumOf { pos ->
            Direction.entries.count { dir ->
                findPart1(field, pos, dir)
            }
        }
    }

    fun findPart2(
        field: Field,
        startPosition: Position,
        direction: Direction,
        startPosition2: Position,
        direction2: Direction
    ): Boolean =
         field.getSafe(startPosition.travel(direction, steps = 1)) == 'A' &&
                field.getSafe(startPosition.travel(direction, steps = 2)) == 'S' &&
                field.getSafe(startPosition2.travel(direction2, steps = 0)) == 'M' &&
                field.getSafe(startPosition2.travel(direction2, steps = 1)) == 'A' &&
                field.getSafe(startPosition2.travel(direction2, steps = 2)) == 'S'

    fun part2(input: List<String>): Int {
        val field = inputToField(input)
        val startPositions = field.findAll { it == 'M' }

        return startPositions.sumOf { pos ->
            listOf(
                DIAGONAL1FORWARD to (DIAGONAL2FORWARD to EAST),
                DIAGONAL1FORWARD to (DIAGONAL2BACKWARD to SOUTH),

                DIAGONAL1BACKWARD to (DIAGONAL2FORWARD to NORTH),
                DIAGONAL1BACKWARD to (DIAGONAL2BACKWARD to WEST),
            ).count { input ->
                val dir1 = input.first
                val dir2 = input.second.first
                val offset = input.second.second

                val pos2 = pos.travel(offset, steps = 2)
                findPart2(field, pos, dir1, pos2, dir2)
            }
        }
    }

    testFile(
        "Part 1 Test 1",
        "Day04_test",
        {
            part1(it)
        },
        18,
        filterBlank = false,
    )
    val input = readInput("Day04").filter(String::isNotBlank)
    part1(input).println()

    testFile(
        "Part 2 Test 1",
        "Day04_test",
        ::part2,
        9,
        filterBlank = false,
    )
    val input2 = readInput("Day04").filter(String::isNotBlank)
    part2(input2)
        .println()
}
