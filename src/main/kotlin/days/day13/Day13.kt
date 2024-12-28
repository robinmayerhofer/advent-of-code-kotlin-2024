package days.day13

import utils.findAllNumbersLong
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

fun main() {
    shouldLog = true

    fun cheapestCost(ax: Long, ay: Long, bx: Long, by: Long, px: Long, py: Long): Long? {
        // This does not solve the optimization problem but only works if
        // there is only at most one valid solution to the problem (i.e., the task is not an optimization problem).
        // In the input, this is ensured by ax/ay or bx/by having at least one prime that is different.
        // This can be checked by prime factoring the input and comparing sets.
        // i.e., this condition holds for all inputs:
        // (primeFactors(ax).toSet() - primeFactor(bx).toSet()).isNotEmpty ||
        // (primeFactors(bx).toSet() - primeFactor(ax).toSet()).isNotEmpty ||
        // (primeFactors(ay).toSet() - primeFactor(by).toSet()).isNotEmpty ||
        // (primeFactors(yb).toSet() - primeFactor(ya).toSet()).isNotEmpty
        //
        // Lets solve the equation system (no cost needs to be considered):
        // #1: ax*na + bx*nb = px
        // #2: ay*na + by*nb = py
        // =>
        // #3: ay*ax*na + ay*bx*nb = ay*px // #1 * ay
        // #4:  = ax*py // #2 * ax
        // =>
        // #5: ay*px - (ay*ax*na + ay*bx*nb) = 0 // based on #3
        // #6: ax*py - (ax*ay*na + ax*by*nb) = 0 // based on #4
        // =>
        // #7: ay*px - (ay*ax*na + ay*bx*nb) = ax*py - (ax*ay*na + ax*by*nb)
        // =>
        // #8: ay*px - ax*py = ay*bx*nb + ay*ax*na - ax*ay*na - ax*by*nb
        // =>
        // #9: ay*px - ax*py = (ay*bx - ax*by)*nb
        // #10: isolate nb in #9
        val nb = ((ay * px - ax * py) / (ay * bx - ax * by))

        // #11: isolate na in #1
        val na = ((px - bx * nb) / ax)

        // Verify the solution satisfies the equations
        return if ((na * ax + nb * bx == px) && (na * ay + nb * by == py)) {
            3 * na + nb
        } else {
            null
        }
    }

    fun part1(input: List<String>): Long =
        input.chunked(3).sumOf { abp ->
            val (dxA, dyA) = abp[0].findAllNumbersLong().let { it[0] to it[1] }
            val (dxB, dyB) = abp[1].findAllNumbersLong().let { it[0] to it[1] }
            val (px, py) = abp[2].findAllNumbersLong().let { it[0] to it[1] }

            cheapestCost(dxA, dyA,dxB, dyB, px, py) ?: 0
        }

    fun part2(input: List<String>): Long {
        val largeN = "10000000000000".toLong()

        return input.chunked(3).sumOf { abp ->
            val (dxA, dyA) = abp[0].findAllNumbersLong().let { it[0] to it[1] }
            val (dxB, dyB) = abp[1].findAllNumbersLong().let { it[0] to it[1] }
            val (px, py) = abp[2].findAllNumbersLong().let { it[0] + largeN to it[1] + largeN }

            cheapestCost(dxA, dyA,dxB, dyB, px, py) ?: 0
        }
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day13_test",
        {
            part1(it.filter(String::isNotBlank))
        },
        480,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day13").filter(String::isNotBlank)
        part1(input)
    }.println()

    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day13").filter(String::isNotBlank)
        part2(input)
    }
        .println()

}
