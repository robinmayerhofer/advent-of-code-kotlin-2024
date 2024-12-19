package client

import client.Submit.SubmissionResult.CORRECT
import client.Submit.SubmissionResult.INCORRECT
import client.Submit.SubmissionResult.INCORRECT_TOO_HIGH
import client.Submit.SubmissionResult.INCORRECT_TOO_LOW
import client.Submit.SubmissionResult.WAIT
import client.Submit.SubmissionResult.WRONG_PART
import utils.twoDigitString
import java.nio.file.Path
import kotlin.io.path.appendLines
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.io.path.readLines

object Submit {

    private data class Submission(
        val value: String,
        val result: SubmissionResult,
    ) {
        companion object {
            fun parse(string: String): Submission {
                val parts = string.split(",", limit = 2)
                return Submission(
                    result = SubmissionResult.valueOf(parts[0]),
                    value = parts[1],
                )
            }
        }

        fun serialize(): String = "${result.name},$value"
    }

    enum class SubmissionResult {
        WAIT,
        INCORRECT,
        INCORRECT_TOO_LOW,
        INCORRECT_TOO_HIGH,
        CORRECT,
        WRONG_PART; // submitting part 2 but part 1 not done yet

        companion object {
            fun parse(response: String): SubmissionResult =
                when {
                    "That's not the right answer" in response -> {
                        when {
                            "your answer is too low" in response -> INCORRECT_TOO_LOW
                            "your answer is too high" in response -> INCORRECT_TOO_HIGH
                            else -> INCORRECT
                        }
                    }

                    "You gave an answer too recently" in response -> WAIT
                    else -> CORRECT
                }

        }
    }

    fun submit(value: Int, day: Day, year: Year = Year(2024), part: Part) =
        submit(value.toString(), day, year, part)

    fun submit(value: Long, day: Day, year: Year = Year(2024), part: Part) =
        submit(value.toString(), day, year, part)

    fun submit(value: String, day: Day, year: Year = Year(2024), part: Part) {
        println("Submitting $value")
        val submissions = getSubmissionsUntilNow(day, year, part)

        if (submissions.any { it.result == CORRECT }) {
            println("Already submitted a correct solution, skipping.")
            return
        }
        submissions.forEach {
            when (it.result) {
                INCORRECT -> check(it.value != value) { "Tried $value already, was incorrect." }
                INCORRECT_TOO_HIGH -> check(value < it.value) { "$value is incorrect because ${it.value} was too high." }
                INCORRECT_TOO_LOW -> check(value > it.value) { "$value is incorrect because ${it.value} was too low." }
                WAIT, WRONG_PART, CORRECT -> Unit
            }
        }

        val response = Client.shared.sendSubmitRequest(value = value, day = day, year = year, part = part)
        val responseBody = response.body()
            .also {
                printResponse(it)
            }
        val result = SubmissionResult.parse(responseBody)

        if (result != WAIT) {
            addSubmission(Submission(value, result), day = day, year = year, part = part)
        }
        if (result != CORRECT) {
            error("$result")
        }
    }

    private fun submissionFile(day: Day, year: Year, part: Part): Path =
        Path.of("src/Day${day.value.twoDigitString()}_${year.value}_part${part.value}_submission.txt")

    private fun getSubmissionsUntilNow(day: Day, year: Year, part: Part): List<Submission> =
        (submissionFile(day, year, part).takeIf { it.exists() }?.readLines() ?: emptyList())
            .map { line ->
                Submission.parse(line)
            }

    private fun addSubmission(submission: Submission, day: Day, year: Year, part: Part) {
        val path = submissionFile(day, year, part)
        if (path.notExists()) {
            path.createFile()
        }
        path.appendLines(listOf(submission.serialize()))
    }

    private fun printResponse(responseBody: String) {
        val regex = Regex("<p>(.*?)</p>")
        val matchResult = regex.find(responseBody)

        if (matchResult != null) {
            val message = matchResult.groupValues[1]
            println(message)
        } else {
            println(responseBody)
        }
    }
}

fun main() {
    Submit.submit(1234567, Day(1), year = Year(2024), part = Part(1))
}
