package days.day17

import client.Day
import client.Download.downloadInput
import client.Year
import utils.findAllNumbers
import utils.findAllNumbersLong
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile
import kotlin.math.pow

fun main() {
    shouldLog = true
    val day = Day(17)
    val year = Year(2024)
    downloadInput(day, year)

    class Simulator(
        var a: Long,
        var b: Long,
        var c: Long,
        val program: List<Int>,
    ) {
        val output = mutableListOf<Int>()

        fun simulate(): String {
            var i = -2

            fun readComboOperand(): Long = when (program[i + 1]) {
                0, 1, 2, 3 -> program[i + 1].toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> error("Will not appear")
            }

            fun readLiteralOperand(): Int = program[i + 1]

            while (i + 2 < program.size) {
                i += 2
                val opcode = program[i]
                when (opcode) {
                    0 -> { // adv
                        val numerator = a
                        val combo = readComboOperand()
                        val denominator = 2.0.pow(combo.toDouble()).toLong()
                        a = numerator / denominator
                    }

                    1 -> { // bxl
                        val literal = readLiteralOperand().toLong()
                        b = b xor literal
                    }

                    2 -> { // bst
                        val combo = readComboOperand()
                        b = combo % 8
                    }

                    3 -> { // jnz
                        if (a != 0L) i =
                            readLiteralOperand() - 2 // "i not increased by two after this instruction"
                    }

                    4 -> { // bxc
                        val result = b xor c
                        b = result
                    }

                    5 -> { // out
                        val result = readComboOperand() % 8
                        output.add(result.toInt())
                    }

                    6 -> { // bdv
                        val numerator = a
                        val combo = readComboOperand()
                        val denominator = 2.0.pow(combo.toDouble()).toInt()
                        b = numerator / denominator
                    }

                    7 -> { // cdv
                        val numerator = a
                        val combo = readComboOperand()
                        val denominator = 2.0.pow(combo.toDouble()).toInt()
                        c = numerator / denominator
                    }

                    else -> error("Should not happen")
                }
            }

            return output.joinToString(separator = ",")
        }
    }

    fun part1(input: List<String>): String {
        val a = input[0].findAllNumbersLong()[0]
        val b = input[1].findAllNumbersLong()[0]
        val c = input[2].findAllNumbersLong()[0]
        val program = input[4].findAllNumbers()

        val sim = Simulator(a, b, c, program)
        sim.simulate()
        return sim.output.joinToString(separator = ",")
    }


    fun part2(input: List<String>): Long {
        val program = input[4].findAllNumbers()

        fun computeOutput(a: Long): Int {
            val mod8 = (a % 8).toInt()
            val xor5 = mod8 xor 5
            val xor6 = xor5 xor 6
            val div = (a / 2.0.pow(xor5.toDouble())).toLong()
            return (xor6 xor (div % 8).toInt())
        }

        var answers = listOf(0L)
        for (p in program.reversed()) {
            val ls = mutableListOf<Long>()
            for (curr in answers) {
                // try all values for the new last 3 bits
                // some answers are dead ends - we need to check ALL of them
                for (aLower in 0..7) {
                    val a = (curr shl 3) + aLower
                    val out = computeOutput(a)
                    if (out == p) {
                        ls.add(a)
                    }
                }
            }
            answers = ls
        }

        return answers.minOrNull() ?: 0L
    }

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day17_test",
        {
            part1(it)
        },
        "4,6,3,5,6,3,5,2,1,0",
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day17")
        part1(input)
    }
        .println()

    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day17")
        part2(input)
    }
        .println()
}
