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
import utils.twoDigitString

data class Gate(
    val input1: String,
    val operation: String,
    val input2: String,
    val output: String,
) {
    fun hasDirectBitInput(): Boolean = input1.startsWith("x") || input2.startsWith("x")
}

data class Wire(val name: String, val value: Boolean)

val regex = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex()

fun main() {
    shouldLog = true
    val day = Day(24)
    val year = Year(2024)
    downloadInput(day, year)

    fun parseInput(input: List<String>): Pair<List<Wire>, MutableList<Gate>> {
        val wires = input.takeWhile { it.isNotBlank() }
            .map { line ->
                val (lhs, rhs) = line.split(":").map { it.trim() }
                Wire(name = lhs, value = rhs.toInt() == 1)
            }

        val gates: MutableList<Gate> = input.drop(wires.size + 1).map {
            val (input1, operation, input2, output) = regex.find(it)!!.destructured
            Gate(output = output, input1 = input1, input2 = input2, operation = operation)
        }.toMutableList()

        return wires to gates
    }

    fun part1(input: List<String>): Long {
        val (wires, gates) = parseInput(input)

        val known: MutableMap<String, Boolean> = mutableMapOf()
        known.putAll(wires.associate { it.name to it.value })

        while (gates.isNotEmpty()) {
            val toRemove = mutableListOf<Gate>()
            for (connection in gates) {
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
            gates.removeAll(toRemove)
        }

        return known
            .filterKeys { it.startsWith("z") }
            .toSortedMap()
            .map { if (it.value) 1 else 0 }
            .joinToString("")
            .reversed()
            .toLong(radix = 2)
    }

    fun generateGraphViz(wires: List<Wire>, gates: List<Gate>): String {
        val nodes = mutableListOf<String>()
        val edges = mutableListOf<String>()

        wires.forEach { wire ->
            nodes.add("\"${wire.name}\" [label=\"${wire.name}\\n${wire.value}\"];")
        }

        gates.forEach { gate ->
            val operationNode = "${gate.input1}_${gate.operation}_${gate.input2}"
            nodes.add("\"$operationNode\" [label=\"${gate.operation}\"];")
            edges.add("\"${gate.input1}\" -> \"$operationNode\";")
            edges.add("\"${gate.input2}\" -> \"$operationNode\";")
            edges.add("\"$operationNode\" -> \"${gate.output}\";")
        }

        return buildString {
            append("digraph G {\n")
            nodes.forEach { append("    $it\n") }
            edges.forEach { append("    $it\n") }
            append("}")
        }
    }

    fun part2(input: List<String>): String {
        val (wires, gates) = parseInput(input)
        generateGraphViz(wires, gates).println()

        // First adder is a HALF ADDER
        // xor0: A XOR B  ->  OUT
        // and0: A AND B  ->  COUT
        // All other adders are (ripple carry) full adders
        // The most significant bit is the carry out of the last adder
        //
        // xor0: A  XOR B    ->  V0
        // and0: A  AND B    ->  V1
        // and1: V0 AND CIN  ->  V2
        // xor1: V0 XOR CIN  ->  OUT
        // or2:  V1 OR  V2   ->  COUT

        val suspicious = mutableSetOf<String>()
        val xor0s = gates
            .filter { it.hasDirectBitInput() }
            .filter { it.operation == "XOR" }
        val xor1s = gates
            .filter { it.operation == "XOR" }
            .filter { !it.hasDirectBitInput() }
        val outGates = gates.filter { it.output.startsWith("z") }

        fun findIncorrectXor0ForHalfAdder() {
            for (g in xor0s) {
                if ((g.input1 == "x00" || g.input2 == "x00") && g.output != "z00") {
                    suspicious.add(g.output)
                } else if ((g.input1 == "y00" || g.input2 == "y00") && g.output != "z00") {
                    suspicious.add(g.output)
                }
            }
        }

        fun findXor0sRoutingToOut() {
            for (g in xor0s) {
                val isFirst = g.input1 == "x00" || g.input2 == "x00"
                when {
                    isFirst -> if (g.output != "z00") {
                        suspicious.add(g.output)
                    }

                    g.output.startsWith("z") -> suspicious.add(g.output)
                }
            }
        }

        fun findXor1sNotRoutingToOut() {
            for (gate in xor1s) {
                if (!gate.output.startsWith("z")) {
                    suspicious.add(gate.output)
                }
            }
        }

        fun findOutGatesThatAreNotXorOrLastAndOr() {
            for (gate in outGates) {
                val isLast = gate.output == "z${(wires.size / 2).twoDigitString()}"
                when {
                    isLast -> if (gate.operation != "OR") {
                        suspicious.add(gate.output)
                    }

                    gate.operation != "XOR" -> suspicious.add(gate.output)
                }
            }
        }

        fun findXor0OutputsThatAreNotXor1ReturnWrongXor0s(): List<Gate> {
            val wrongXor0s = mutableListOf<Gate>()

            for (g in xor0s) {
                if (suspicious.contains(g.output)) continue
                if (g.output == "z00") continue
                val matches = xor1s.filter { it.input1 == g.output || it.input2 == g.output }
                if (matches.isEmpty()) {
                    wrongXor0s.add(g)
                    suspicious.add(g.output)
                }
            }

            return wrongXor0s
        }

        fun findMissing(wrongXor0s: List<Gate>) {
            for (g in wrongXor0s) {
                val out = "z${g.input1.substring(1)}" // inputs have same number, just different letter
                val xor1 = xor1s
                    .filter { it.output == out }
                    .also {
                        check(it.size == 1) {
                            "Unexpected number of matches (${it.size}). XOR1 should have 1 OR (cout of previous adder)"
                        }
                    }
                    .get(0)

                val xor1Inputs = listOf(xor1.input1, xor1.input2)
                val orInputForXor1 = gates
                    .filter { it.operation == "OR" }
                    .filter { it.output in xor1Inputs }
                    .also {
                        check(it.size == 1) {
                            "Unexpected number of matches. Wiring is more messed up than this solver is capable of solving."
                        }
                    }
                    .get(0)
                    .output

                val correctOutput = xor1Inputs.find { it != orInputForXor1 } ?: error("Not found!")
                suspicious.add(correctOutput)
            }
        }

        findIncorrectXor0ForHalfAdder()
        findXor0sRoutingToOut()
        findXor1sNotRoutingToOut()
        findOutGatesThatAreNotXorOrLastAndOr()
        val wrongXor0s = findXor0OutputsThatAreNotXor1ReturnWrongXor0s()
        findMissing(wrongXor0s)

        check(suspicious.size == 8)
        return suspicious.sorted().joinToString(",")
    }

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

    shouldLog = false
    println("Solving part 2")
    measure {
        val input = readInput("Day24")
        part2(input)
    }
        .also {
            submit(it, day, year, Part(2))
        }
        .println()
}
