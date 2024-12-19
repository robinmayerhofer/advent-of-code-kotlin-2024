package client

import utils.twoDigitString
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes
import kotlin.io.path.writeText

private val dayOfMonth: Int by lazy {
    System.getenv()["DAY"]?.toInt() ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
}

object Download {
    private fun fetchInput(day: Day, year: Year): String {
        val availableAt = ZonedDateTime.of(year.value, 12, day.value, 0, 0, 0, 0, ZoneId.of("US/Eastern"))
        val now = ZonedDateTime.now()
        val timeToWait: Duration = if (availableAt < now) {
            Duration.ZERO
        } else {
            Duration.ofMillis(
                ChronoUnit.MILLIS.between(now, availableAt)
            )
        }

        if (!timeToWait.isZero) {
            println("Waiting: $timeToWait")
            Thread.sleep(timeToWait.toMillis())
        }

        val response = Client.shared.sendInputDownloadRequest(day = day, year = year)
        return response.body()
    }

    fun downloadInput(day: Day, year: Year = Year(2024)) {
        val target = Path("src/Day${dayOfMonth.twoDigitString()}.txt")
        if (target.exists() && target.readBytes().isNotEmpty()) {
            println("Skipping download - already exists and has data.")
            return
        }
        target.writeText(
            fetchInput(day = Day(dayOfMonth), year = year).also { println("Input:\n$it") }
        )
    }
}

fun main() {
    Download.downloadInput(day = Day(dayOfMonth))
}
