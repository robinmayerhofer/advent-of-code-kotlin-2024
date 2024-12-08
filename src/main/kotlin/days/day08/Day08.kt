package days.day08

import utils.Field
import utils.Position
import utils.findAll
import utils.inputToField
import utils.isValidPosition
import utils.println
import utils.readInput
import utils.set
import utils.shouldLog
import utils.testFile

typealias Finder = ((Field, Position, Position) -> List<Position>)

fun main() {
    shouldLog = false

    fun findPositionsForAntennas(
        field: Field,
        antennas: List<Position>,
        finder: Finder,
    ): Set<Position> {
        val ps = mutableSetOf<Position>()

        for ((i1, pos1) in antennas.withIndex()) {
            for ((i2, pos2) in antennas.withIndex()) {
                if (i2 <= i1) continue
                ps.addAll(finder(field, pos1, pos2))
            }
        }

        return ps
    }

    fun antinodesOnField(
        input: List<String>,
        finder: Finder
    ): Int {
        val f = inputToField(input)
        val chars: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return chars.flatMapTo(mutableSetOf()) { char ->
            val positions = f.findAll { it == char }
            findPositionsForAntennas(f, positions, finder)
        }
            .also { pos ->
                if (shouldLog) {
                    pos.forEach { f[it] = '#' }
                    f.println()
                }
            }
            .size
    }

    fun finderPart1(field: Field, a: Position, b: Position): List<Position> {
        val xDiff = b.row - a.row
        val yDiff = b.column - a.column

        return listOf(
            Position(row = a.row - xDiff, column = a.column - yDiff),
            Position(row = b.row + xDiff, column = b.column + yDiff),
        ).filter { field.isValidPosition(it) }
            .also {
                if (shouldLog) println("Had $a and $b, found $it")
            }
    }

    fun part1(input: List<String>): Int =
        antinodesOnField(input, ::finderPart1)

    fun finderPart2(field: Field, a: Position, b: Position): List<Position> {
        val xDiff = b.row - a.row
        val yDiff = b.column - a.column

        fun generatePositions(start: Position, xStep: Int, yStep: Int): List<Position> = buildList {
            var current = start
            while (true) {
                current = Position(row = current.row + xStep, column = current.column + yStep)
                if (field.isValidPosition(current)) {
                    add(current)
                } else {
                    break
                }
            }
        }

        return listOf(a, b) + generatePositions(a, -xDiff, -yDiff) + generatePositions(b, xDiff, yDiff)
    }

    fun part2(input: List<String>): Int =
        antinodesOnField(input, ::finderPart2)

    testFile(
        "Part 1 Test 1",
        "Day08_test",
        {
            part1(it)
        },
        14,
        filterBlank = false,
    )
    val input = readInput("Day08").filter(String::isNotBlank)
    part1(input).println()

    testFile(
        "Part 2 Test 1",
        "Day08_test",
        ::part2,
        34,
        filterBlank = false,
    )
    val input2 = readInput("Day08").filter(String::isNotBlank)
    part2(input2).println()
}
