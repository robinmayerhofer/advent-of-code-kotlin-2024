package days.day14

import utils.DigitField
import utils.Position
import utils.findAllNumbers
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true

    data class Robot(
        val px: Int,
        val py: Int,
        val vx: Int,
        val vy: Int,
    )

    fun List<Robot>.debugPrint(sizeX: Int, sizeY: Int) {
        val f = DigitField(size = sizeY) { IntArray(sizeX) { 0 } }
        forEach { f[it.py][it.px]++ }
        f.joinToString("\n") { pos ->
            pos.joinToString("") {
                if (it == 0) "." else "$it"
            }
        }
            .println()
    }

    fun step(robots: List<Robot>, sizeX: Int, sizeY: Int, steps: Int): List<Robot> =
        robots
            .map { r ->
                Robot(
                    px = Math.floorMod(r.px + r.vx * steps, sizeX),
                    py = Math.floorMod(r.py + r.vy * steps, sizeY),
                    vx = r.vx,
                    vy = r.vy
                )
            }

    fun List<String>.parse(): List<Robot> =
        filter { it.isNotBlank() }
            .map {
                val ns = it.findAllNumbers()
                Robot(ns[0], ns[1], ns[2], ns[3])
            }

    fun part1(input: List<String>, sizeX: Int, sizeY: Int, steps: Int = 100): Int {
        val robots = step(
            robots = input.parse(),
            sizeX = sizeX,
            sizeY = sizeY,
            steps = steps
        )

        if (shouldLog) robots.debugPrint(sizeX, sizeY)

        var q11 = 0
        var q12 = 0
        var q21 = 0
        var q22 = 0

        assert(sizeX % 2 == 1 && sizeY % 2 == 1)
        val midX = sizeX / 2
        val midY = sizeY / 2

        for (r in robots) {
            when {
                r.px < midX && r.py < midY -> q11++
                r.px < midX && r.py > midY -> q21++
                r.px > midX && r.py < midY -> q12++
                r.px > midX && r.py > midY -> q22++
            }
        }

        return q11 * q12 * q21 * q22
    }

    fun part2(input: List<String>, sizeX: Int, sizeY: Int): Int {
        val robots = input
            .filter { it.isNotBlank() }
            .map {
                val ns = it.findAllNumbers()
                Robot(ns[0], ns[1], ns[2], ns[3])
            }

        var i = 0
        var new: List<Robot>
        // assumption (that worked):
        // all positions are unique - no overlaps ("image existed and input was created by moving backwards")
        do {
            new = step(robots, sizeX = sizeX, sizeY = sizeY, steps = i)
            i++
        } while (new.map { Position(it.px, it.py) }.toSet().size != robots.size)

        new.debugPrint(101, 103)
        return i
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day14_test",
        {
            part1(it, sizeX = 11, sizeY = 7)
        },
        12,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day14").filter(String::isNotBlank)
        part1(input, sizeX = 101, sizeY = 103)
    }.println()

    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day14").filter(String::isNotBlank)
        part2(input, sizeX = 101, sizeY = 103)
    }.println()
}
