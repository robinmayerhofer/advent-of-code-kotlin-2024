package days.day24

import client.Day
import client.Download.downloadInput
import client.Part
import client.Submit.submit
import client.Year
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

data class Gate(
    val input1: String,
    val operation: String,
    val input2: String,
    val output: String,
)

data class Wire(val name: String, val value: Boolean)

// extract, "rmj, khc" "OR", "cqp" out of "rmj OR khc -> cqp"
// with a regex
val regex = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex()

fun main() {
    shouldLog = true
    val day = Day(24)
    val year = Year(2024)
    downloadInput(day, year)

    fun part1(input: List<String>): Long {
        val inputs = input.takeWhile { it.isNotBlank() }
            .map { line ->
                val (lhs, rhs) = line.split(":").map { it.trim() }
                Wire(name = lhs, value = rhs.toInt() == 1)
            }

        val connections: MutableList<Gate> = input.drop(inputs.size + 1).map {
            val (input1, operation, input2, output) = regex.find(it)!!.destructured
            Gate(output = output, input1 = input1, input2 = input2, operation = operation)
        }.toMutableList()

        val known: MutableMap<String, Boolean> = mutableMapOf()
        known.putAll(inputs.associate { it.name to it.value })

        while (connections.isNotEmpty()) {
            val toRemove = mutableListOf<Gate>()
            for (connection in connections) {
                val input1 = known[connection.input1]
                val input2 = known[connection.input2]
                if (input1 != null && input2 != null) {
                    val value = when (connection.operation) {
                        "AND" -> input1 && input2
                        "OR" -> input1 || input2
                        "XOR" -> input1 xor input2
                        else -> error("Unknown operation ${connection.operation}")
                    }
                    known[connection.output] = value
                    toRemove.add(connection)
                }
            }
            connections.removeAll(toRemove)
        }

        return known
            .filterKeys { it.startsWith("z") }
            .toSortedMap()
            .map { if (it.value) 1 else 0 }
            .joinToString("")
            .reversed()
            .toLong(radix = 2)
    }

    fun part2(input: List<String>): Int =
        input.size

    shouldLog = true
    testFile(
        "Part 1 Test 1",
        "Day24_test",
        {
            part1(it)
        },
        4,
        filterBlank = false,
    )

    shouldLog = true
    testFile(
        "Part 1 Test 2",
        "Day24_test2",
        {
            part1(it)
        },
        2024,
        filterBlank = false,
    )

    shouldLog = false
    println("Solving part 1")
    measure {
        val input = readInput("Day24")
        part1(input)
    }
        .also {
            submit(it, day, year, Part(1))
        }
        .println()

//    shouldLog = true
//    testFile(
//        "Part 2 Test 1",
//        "Day24_test",
//        ::part2,
//        1,
//        filterBlank = false,
//    )
//    shouldLog = false
//    println("Solving part 2")
//    measure {
//        val input = readInput("Day24")
//        part2(input)
//    }
//        .also {
//            submit(it, day, year, Part(2))
//        }
//        .println()
}
