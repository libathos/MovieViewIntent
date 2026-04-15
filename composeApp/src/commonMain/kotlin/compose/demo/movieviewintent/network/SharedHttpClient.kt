package compose.demo.movieviewintent.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.statement.request
import kotlinx.serialization.json.Json

/**
 * Shared Ktor [HttpClient] configured for all platforms with:
 * - Base URL: https://api.themoviedb.org/3/
 * - JSON serialization (kotlinx.serialization)
 * - Basic logging
 */
val httpClient: HttpClient by lazy {
    HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                    prettyPrint = false
                }
            )
        }

        install(Logging) {
            // Avoid relying on Logger.DEFAULT to be compatible across Ktor versions
            logger = object : Logger {
                override fun log(message: String) {
                    // Keep logging lightweight and cross-platform (visible in Android Logcat as System.out)
                    println("[HTTP] $message")
                }
            }
            // Log request and response lines, headers, and bodies
            level = LogLevel.BODY
        }

        // Emit a concise line per response for easy grepping in Logcat
        install(ResponseObserver) {
            onResponse { response ->
                val url = response.request.url.toString()
                val status = response.status.value
                println("[HTTP_RESPONSE] status=$status url=$url")
            }
        }

        // Apply the base URL and default headers to every request
        defaultRequest {
            url("https://api.themoviedb.org/3/")
            accept(ContentType.Application.Json)
        }
    }
}
