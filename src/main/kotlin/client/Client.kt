package client

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import kotlin.io.path.Path
import kotlin.io.path.readText

class Client private constructor(
    private val userAgent: String = "github.com/robinmayerhofer/advent-of-code-kotlin-2024",
    private val sessionCookie: String,
) {
    companion object {
        val shared: Client by lazy {
            val sessionCookie = Path("src/.session_cookie").readText().trim()
            Client(sessionCookie = sessionCookie)
        }
    }

    private val httpClient: HttpClient by lazy {
        HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .build()
    }

    fun sendInputDownloadRequest(year: Year, day: Day): HttpResponse<String> {
        val request = HttpRequest.newBuilder()
            .uri(URI("https://adventofcode.com/${year.value}/day/${day.value}/input"))
            .GET()
            .header("User-Agent", userAgent)
            .header("Cookie", "session=$sessionCookie")
            .header("Accept", "*/*")
            .timeout(Duration.ofSeconds(5))
            .build()

        return httpClient.send(request, BodyHandlers.ofString())
            .also {
                assert(it.statusCode() in 200..299) {
                    "Failed to download - ${it.statusCode()}\n${it.body()}\n=> Exiting."
                }
            }
    }

    fun sendSubmitRequest(value: Long, day: Day, year: Year = Year(2024), part: Part): HttpResponse<String> {
        val request = HttpRequest.newBuilder()
            .uri(URI("https://adventofcode.com/$year/day/$day/answer"))
            .header("User-Agent", userAgent)
            .header("Cookie", "session=$sessionCookie")
            .header("Accept", "*/*")
            .headers("Content-Type", "application/x-www-form-urlencoded")
            .timeout(Duration.ofSeconds(5))
            .POST(BodyPublishers.ofString("level=$part&answer=$value"))
            .build()

        return httpClient.send(request, BodyHandlers.ofString())
            .also {
                assert(it.statusCode() in 200..299) {
                    "Failed to submit - ${it.statusCode()}\n${it.body()}\n=> Exiting."
                }
            }
    }

}
