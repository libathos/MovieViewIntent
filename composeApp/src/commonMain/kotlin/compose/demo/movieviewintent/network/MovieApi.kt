package compose.demo.movieviewintent.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Simple TMDB API wrapper using the shared Ktor [httpClient].
 * Base URL is configured in [SharedHttpClient].
 */
object MovieApi {
    const val API_KEY = "e8d648003bd11b5c498674fbd4905525"

    /**
     * Fetches the Top Rated movies from TMDB.
     *
     * Mirrors the Retrofit-style signature provided by the user, but returns the decoded body directly.
     * Ktor throws an exception for non-2xx responses because `expectSuccess = true`.
     */
    suspend fun getTopRatedMovies(
        language: String = "en-US",
        page: Int
    ): MovieResponse {
        return httpClient.get("movie/top_rated") {
            parameter("api_key", API_KEY)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }
}

@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>
)

@Serializable
data class MovieDto(
    // Field names match TMDB JSON, so @SerialName not strictly necessary here
    val original_title: String,
    val poster_path: String? = null,
    val overview: String
) {
    /** Local-only ID default; not part of the serialized payload. */
    @Transient
    var id: Int = -1
}
