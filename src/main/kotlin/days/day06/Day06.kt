package days.day06

import utils.Direction
import utils.Field
import utils.Position
import utils.find
import utils.get
import utils.inputToField
import utils.isValidPosition
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import utils.travel

fun main() {
    shouldLog = true

    fun part1(input: List<String>): Int {
        val field = inputToField(input)

        var currPos = field.find { it == '^' }
        var direction = Direction.UP
        val visited = mutableSetOf<Pair<Position, Direction>>()

        while (true) {
            if (!visited.add(currPos to direction)) {
                break
            }
            val nextPos = currPos.travel(direction)
            if (!field.isValidPosition(nextPos)) {
                break
            }
            if (field[nextPos] == '#') {
                direction = direction.turnRight()
                continue
            } else {
                currPos = nextPos
            }
        }
        return visited.map { it.first }.toSet().size
    }

    fun getsStuckInLoop(field: Field, modifiedPosition: Position): Boolean {
        var currPos = field.find { it == '^' }
        var direction = Direction.UP
        val visited = mutableSetOf<Pair<Position, Direction>>()

        while (true) {
            if (!visited.add(currPos to direction)) {
                return true
            }
            val nextPos = currPos.travel(direction)
            if (!field.isValidPosition(nextPos)) {
                return false
            }
            if (nextPos == modifiedPosition || field[nextPos] == '#') {
                direction = direction.turnRight()
                continue
            } else {
                currPos = nextPos
            }
        }
    }

    fun part2(input: List<String>): Int {
        val field = inputToField(input)

        var count = 0

        for (row in field.indices) {
            for (column in field[row].indices) {
                val pos = Position(row, column)
                if (field[pos] == '.') {
                    if (getsStuckInLoop(field, pos)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    testFile(
        "Part 1 Test 1",
        "Day06_test",
        {
            part1(it)
        },
        41,
        filterBlank = false,
    )
    val input = readInput("Day06").filter(String::isNotBlank)
    part1(input).println()

    testFile(
        "Part 2 Test 1",
        "Day06_test",
        ::part2,
        6,
        filterBlank = false,
    )
    measure {
        val input2 = readInput("Day06").filter(String::isNotBlank)
        part2(input2).println()
    }
}
