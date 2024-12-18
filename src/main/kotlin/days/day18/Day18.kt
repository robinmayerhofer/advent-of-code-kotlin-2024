package days.day18

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.DigitField
import utils.Direction
import utils.Position
import utils.findAllNumbers
import utils.get
import utils.isValidPosition
import utils.measure
import utils.println
import utils.readInput
import utils.set
import utils.shouldLog
import utils.testFile
import utils.travel
import java.util.PriorityQueue
import javax.swing.text.html.HTML.Tag.P

fun main() {
    shouldLog = true
    val day = Day(18)
    val year = Year(2024)
    downloadInput(day, year)

    data class Vertex(
        val pos: Position,
        val cost: Int,
    ): Comparable<Vertex> {
        override fun compareTo(other: Vertex): Int =
            cost - other.cost
    }

    fun DigitField.debugPrint() = joinToString(separator = "\n") { it.joinToString("") { if (it == 0) "." else "#" } }.println()

    fun findShortestPath(start: Position, end: Position, f: DigitField): Int? {
        val q = PriorityQueue<Vertex>()
        q.add(Vertex(start, 0))

        val dist = mutableMapOf<Position, Int>()

        while (q.isNotEmpty()) {
            val v = q.poll()
            if (dist[v.pos] != null) continue
            if (v.pos == end) return v.cost
            dist[v.pos] = v.cost
            q.addAll(
                listOf(
                    Vertex(v.pos.travel(Direction.UP), v.cost + 1),
                    Vertex(v.pos.travel(Direction.DOWN), v.cost + 1),
                    Vertex(v.pos.travel(Direction.LEFT), v.cost + 1),
                    Vertex(v.pos.travel(Direction.RIGHT), v.cost + 1),
                )
                    .filter { f.isValidPosition(it.pos) }
                    .filter { f[it.pos] == 0 }
                    .filter { dist[it.pos] == null }
            )
        }
        return null
    }

    fun part1(input: List<String>, xs: IntRange, ys: IntRange, amount: Int): Int? {
        val f = Array(ys.count()) {
            IntArray(xs.count()) {
                0
            }
        }

        val obstacles = input
            .map { it.findAllNumbers() }
            .map { Position(column = it[0], row = it[1]) }
            .take(amount)

        obstacles.forEach { f[it] = 1 }
        return findShortestPath(start = Position(0, 0), end = Position(f.size - 1, f.size - 1), f)
            ?: error("Not found")
    }

    fun part2(input: List<String>, xs: IntRange, ys: IntRange): String {
        val obstacles = input
            .map { it.findAllNumbers() }
            .map { Position(column = it[0], row = it[1]) }

        val f = Array(ys.count()) {
            IntArray(xs.count()) {
                0
            }
        }

        for (i in 0..obstacles.size) {
            f[obstacles[i]] = 1

            if (findShortestPath(start = Position(0, 0), end = Position(f.size - 1, f.size - 1), f) == null) {
                return obstacles[i].let { "${it.column},${it.row}" }
            }
        }
        error("Not found")
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day18_test",
        {
            part1(it, xs = 0..6, ys = 0..6, amount = 12)
        },
        22,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day18").filter(String::isNotBlank)
        part1(input, xs = 0..70, ys = 0..70, amount = 1024) ?: error("Not found")
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()

    shouldLog = true
    testFile(
        "Part 2 Test 1",
        "Day18_test",
        { part2(it, xs = 0..6, ys = 0..6)},
        "6,1",
        filterBlank = false,
    )
    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day18").filter(String::isNotBlank)
        part2(input, xs = 0..70, ys = 0..70)
    }
//        .also {
//            submit(it, day, year, Part(2))
//        }
        .println()
}
