package days.day15

import utils.Direction
import utils.Direction.DOWN
import utils.Direction.LEFT
import utils.Direction.RIGHT
import utils.Direction.UP
import utils.Field
import utils.Position
import utils.debugPrint
import utils.find
import utils.findAll
import utils.get
import utils.inputToField
import utils.measure
import utils.println
import utils.readInput
import utils.set
import utils.shouldLog
import utils.testFile
import utils.travel

fun main() {
    shouldLog = true

    // If `execute = false`: Returns `true` if pushing works, `false` otherwise.
    // If `execute = true`: Pushes, throws if pushing does not work
    fun tryPush(f: Field, dir: Direction, pos: Position, execute: Boolean): Boolean {
        val curr = f[pos]
        val tPos = pos.travel(dir)
        val target = f[tPos]

        return when {
            target == '#' -> false
            target == '.' -> true
            target == 'O' -> tryPush(f, dir, tPos, execute)
            dir in setOf(UP, DOWN) -> when (target) {
                '[' -> tryPush(f, dir, tPos, execute) && tryPush(f, dir, tPos.travel(RIGHT), execute)
                ']' -> tryPush(f, dir, tPos, execute) && tryPush(f, dir, tPos.travel(LEFT), execute)
                else -> error("Cannot happen")
            }

            else -> when (target) {
                '[', ']' -> tryPush(f, dir, tPos, execute)
                else -> error("Cannot happen, target is $target")
            }
        }.also {
            if (execute) {
                assert(it)
                f[tPos] = curr
                f[pos] = '.'
            }
        }
    }

    fun push(f: Field, dir: Direction, pos: Position) {
        val canPush = tryPush(f, dir, pos, execute = false)
        if (canPush) {
            tryPush(f, dir, pos, execute = true)
        }
    }

    fun move(f: Field, m: Direction) {
        if (shouldLog) println("Move ${m}:")
        val robot = f.find { it == '@' }
        push(f, m, robot)
        if (shouldLog) {
            f.debugPrint()
            println("")
        }
    }

    fun move(f: Field, ms: List<Direction>) {
        if (shouldLog) {
            println("Initial state:")
            f.debugPrint()
            println("")
        }
        for (m in ms) {
            move(f, m)
        }
    }

    fun List<String>.parse(): Pair<Field, List<Direction>> {
        val split = indexOfFirst { it.isBlank() }
        val f = inputToField(subList(0, split))
        val ms = subList(split + 1, size).joinToString("")
            .map {
                when (it) {
                    '<' -> LEFT
                    '>' -> RIGHT
                    'v' -> DOWN
                    '^' -> UP
                    else -> error("Unexpected input '$it'")
                }
            }
        return f to ms
    }

    fun part1(input: List<String>): Int {
        val (f, ms) = input.parse()
        move(f, ms)
        return f
            .findAll { it == 'O' }
            .sumOf { it.row * 100 + it.column }
    }

    fun scaleUp(f: Field): Field =
        f.map { line ->
            line.flatMap {
                when (it) {
                    '.' -> ".."
                    '#' -> "##"
                    '@' -> "@."
                    'O' -> "[]"
                    else -> error("Unexpected char $it")
                }.toList()
            }.toCharArray()
        }.toTypedArray()

    fun part2(input: List<String>): Int {
        val (f, ms) = input.parse().let { (f, ms) -> scaleUp(f) to ms }
        move(f, ms)
        return f
            .findAll { it == '[' }
            .sumOf { it.row * 100 + it.column }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day15_test",
        ::part1,
        10092,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day15")
        part1(input)
    }.println()

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day15_test",
        ::part2,
        9021,
        filterBlank = false,
    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day15")
        part2(input)
    }.println()
}
