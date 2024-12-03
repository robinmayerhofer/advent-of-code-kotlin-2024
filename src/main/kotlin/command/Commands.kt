package command

import command.Command.*
import utils.findAllNumbers
import utils.shouldLog
import kotlin.reflect.KClass

sealed interface Command {
    data object Do : Command

    data object Dont : Command

    data class Mul(val n1: Int, val n2: Int) : Command {
        val result = n1 * n2
    }
}

object CommandFactory {
    private val detectionRegexMap: Map<KClass<out Command>, Regex> = mapOf(
        Do::class to "do\\(\\)".toRegex(),
        Dont::class to "don't\\(\\)".toRegex(),
        Mul::class to "mul\\(\\d+,\\d+\\)".toRegex()
    )

    private val fullRegex = detectionRegexMap.values.joinToString(
        separator = "|", prefix = "(", postfix = ")"
    ).toRegex()

    fun parse(input: List<String>) = parse(input.joinToString("\n"))

    fun parse(string: String): Sequence<Command> =
        fullRegex
            .findAll(string)
            .map { matchResult ->
                val detection = matchResult.value
                val command = detectionRegexMap.entries
                    .first { it.value.matches(detection) }
                    .key
                mapDetection(command, detection)
            }

    private fun mapDetection(commandClass: KClass<out Command>, value: String): Command =
        when (commandClass) {
            Do::class -> Do
            Dont::class -> Dont
            Mul::class -> {
                val ns = value.findAllNumbers()
                Mul(ns[0], ns[1])
            }
            else -> error("Unknown command")

        }.also { if (shouldLog) { println("Found $it") } }

}

class CommandInterpreter(private val commands: Sequence<Command>) {
    private var enabled: Boolean = true
    private var sum: Int = 0

    fun execute(): Int {
        for (command in commands) {
            when (command) {
                Do -> enabled = true
                Dont -> enabled = false
                is Mul -> if (enabled) {
                    sum += command.result
                }
            }
        }
        return sum
    }
}
