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

    /**
     * Fetches movie details by id.
     */
    suspend fun getMovieDetails(
        movieId: Int,
        language: String = "en-US"
    ): MovieDetailsDto {
        return httpClient.get("movie/$movieId") {
            parameter("api_key", API_KEY)
            parameter("language", language)
        }.body()
    }

    /**
     * Fetches movie credits (cast and crew) by id.
     */
    suspend fun getMovieCredits(
        movieId: Int,
        language: String = "en-US"
    ): MovieCreditsDto {
        return httpClient.get("movie/$movieId/credits") {
            parameter("api_key", API_KEY)
            parameter("language", language)
        }.body()
    }

    /**
     * Fetches movie videos by id.
     */
    suspend fun getMovieVideos(
        movieId: Int,
        language: String = "en-US"
    ): MovieVideosDto {
        return httpClient.get("movie/$movieId/videos") {
            parameter("api_key", API_KEY)
            parameter("language", language)
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
     // TMDB payload fields
     val id: Int = -1,
     val original_title: String,
     val poster_path: String? = null,
     val overview: String
 )

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val original_title: String,
    val poster_path: String? = null,
    val overview: String = "",
    val runtime: Int? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null,
    val release_date: String? = null,
    val genres: List<Genre>? = null
)


@Serializable
data class CastDto(
    val id: Int,
    val name: String,
    val original_name: String? = null,
    val character: String? = null,
    val profile_path: String? = null,
    val order: Int? = null,
    val credit_id: String? = null
)

@Serializable
data class CrewDto(
    val id: Int,
    val name: String,
    val original_name: String? = null,
    val job: String? = null,
    val department: String? = null,
    val profile_path: String? = null,
    val credit_id: String? = null
)

@Serializable
data class MovieCreditsDto(
    val id: Int,
    val cast: List<CastDto> = emptyList(),
    val crew: List<CrewDto> = emptyList()
)


@Serializable
data class VideoDto(
    val id: String,
    val name: String,
    val key: String,           // YouTube or Vimeo video key
    val site: String,          // e.g., "YouTube"
    val size: Int? = null,     // e.g., 1080
    val type: String? = null,  // e.g., "Trailer", "Teaser"
    val official: Boolean? = null,
    val published_at: String? = null,
    val iso_639_1: String? = null,
    val iso_3166_1: String? = null
)

@Serializable
data class MovieVideosDto(
    val id: Int,                    // The movie id
    val results: List<VideoDto> = emptyList()
)
