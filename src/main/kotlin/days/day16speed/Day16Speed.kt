package days.day16speed

import utils.Direction
import utils.Direction.Companion.EAST
import java.util.PriorityQueue
import kotlin.math.min
import days.day16speed.DS.MovementState
import days.day16speed.DS.Position
import days.day16speed.DS.Vertx
import days.day16speed.DS.VertxState
import kotlin.io.path.Path
import kotlin.io.path.readLines

object DS {
    data class MovementState(
        val pos: Position,
        val prevDir: Direction,
    )

    class VertxState(
        val cost: Int,
        val prev: MutableList<Vertx>
    )

    class Vertx(
        val move: MovementState,
        val state: VertxState
    ) : Comparable<Vertx> {
        override fun compareTo(other: Vertx): Int =
            state.cost - other.state.cost
    }

    data class Position(val column: Int, val row: Int) {
        fun travel(direction: Direction): Position =
            copy(column = column + direction.deltaX, row = row + direction.deltaY)
    }
}

typealias Field = Array<Array<Char>>

object Day16Speed {
    private lateinit var start: Position
    private lateinit var target: Position
    private lateinit var f: Field
    private var minCost: Int = Int.MAX_VALUE
    private var dist: MutableMap<MovementState, VertxState> = mutableMapOf()

    fun findSize() {
        val queue: ArrayDeque<Vertx> = dist
            .filter { it.key.pos == target && it.value.cost == minCost }
            .flatMap { it.value.prev }
            .let(::ArrayDeque)

        val acc = mutableSetOf(start, target)

        while (true) {
            val vertex = queue.removeFirstOrNull() ?: return print(acc.size)
            acc.add(vertex.move.pos)
            dist[vertex.move]?.prev?.let(queue::addAll)
        }
    }

    // Result to minCost
    fun findAllPaths() {
        val queue =
            PriorityQueue<Vertx>().apply { add(Vertx(MovementState(start, EAST), VertxState(0, mutableListOf()))) }

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
                Triple(current.move.pos.travel(current.move.prevDir), current.move.prevDir, 1),
                current.move.prevDir.turnLeft().let { Triple(current.move.pos.travel(it), it, 1001) },
                current.move.prevDir.turnRight().let { Triple(current.move.pos.travel(it), it, 1001) },
            )
                .map { (pos, dir, costAdd) ->
                    if (f[pos.row][pos.column] == '#') return@map
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
    }

    fun parseInput(input: List<String>) {
        val rows = input.size
        val columns = input.firstOrNull()?.length ?: 0

        f =
            Array(rows) { row ->
                Array(columns) { column ->
                    val x = input[row][column]
                    if (x == 'S') start = Position(column, row)
                    else if (x == 'E') target = Position(column, row)
                    x
                }
            }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = Path(args[0]).readLines()
        parseInput(input)
        findAllPaths()
        findSize()
    }
}

