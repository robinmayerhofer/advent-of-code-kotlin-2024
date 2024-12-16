package days.day16

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.Direction
import utils.Direction.Companion.EAST
import utils.Field
import utils.Position
import utils.find
import utils.get
import utils.inputToField
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import utils.travel
import java.util.PriorityQueue
import kotlin.math.min

private data class MovementState(
    val pos: Position,
    val prevDir: Direction,
)

private data class VertxState(
    val cost: Int,
    val prev: MutableList<Vertx>
)

private data class Vertx(
    val move: MovementState,
    val state: VertxState
) : Comparable<Vertx> {
    override fun compareTo(other: Vertx): Int =
        state.cost - other.state.cost
}

fun main() {
    shouldLog = true
    val day = Day(16)
    val year = Year(2024)
    downloadInput(day, year)

    fun findSize(
        start: Position,
        target: Position,
        result: Map<MovementState, VertxState>,
        minCost: Int
    ): Int {
        val queue: ArrayDeque<Vertx> = result
            .filter { it.key.pos == target && it.value.cost == minCost }
            .flatMap { it.value.prev }
            .let(::ArrayDeque)

        val acc = mutableSetOf(start, target)

        while (true) {
            val vertex = queue.removeFirstOrNull() ?: return acc.size
            acc.add(vertex.move.pos)
            result[vertex.move]?.prev?.let(queue::addAll)
        }
    }

    // Result to minCost
    fun findAllPaths(f: Field, start: Position, target: Position): Pair<Map<MovementState, VertxState>, Int> {
        val queue = PriorityQueue<Vertx>().apply { add(Vertx(MovementState(start, EAST), VertxState(0, mutableListOf()))) }
        val dist = mutableMapOf<MovementState, VertxState>()

        var minCost: Int = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.state.cost > minCost) {
                continue
            }
            val existing = dist[current.move]
            if (existing != null) {
                if (existing.cost == current.state.cost) {
                    existing.prev.addAll(current.state.prev)
                }
                continue
            }

            dist[current.move] = current.state
            if (current.move.pos == target) {
                minCost = min(current.state.cost, minCost)
                continue
            }

            listOf(
                Triple(current.move.pos.travel(current.move.prevDir), current.move.prevDir ,1),
                Triple(current.move.pos, current.move.prevDir.turnLeft() ,1000),
                Triple(current.move.pos, current.move.prevDir.turnRight() ,1000),
            )
                .filterNot { (pos, _, _) -> f[pos] == '#' }
                .map { (pos, dir, costAdd) ->
                    queue.add(
                        Vertx(
                            MovementState(
                                pos,
                                dir
                            ),
                            VertxState(current.state.cost + costAdd, prev = mutableListOf(current))
                        )
                    )
                }
        }

        return dist to minCost
    }

    fun part1(input: List<String>): Int {
        val f = inputToField(input)
        val start = f.find { it == 'S' }
        val target = f.find { it == 'E' }
        return findAllPaths(f, start, target).second
    }

    fun part2(input: List<String>): Int {
        val f = inputToField(input)
        val start = f.find { it == 'S' }
        val target = f.find { it == 'E' }
        return findAllPaths(f, start,target).let {
            findSize(start, target, it.first, it.second)
        }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day16_test",
        {
            part1(it)
        },
        7036,
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

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day16_test",
        ::part2,
        45,
        filterBlank = false,
    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day16").filter(String::isNotBlank)
        part2(input)
    }
        .also {
            submit(it, day, year, Part(2))
        }
        .println()
}
