package compose.demo.movieviewintent.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Simple TMDB API wrapper using the shared Ktor [HttpClient].
 *
 * Accepts [HttpClient] as a constructor parameter for dependency injection.
 */
class MovieApi(private val client: HttpClient) {
    companion object {
        const val API_KEY = "e8d648003bd11b5c498674fbd4905525"
    }

    /**
     * Fetches the Top Rated movies from TMDB.
     */
    suspend fun getTopRatedMovies(
        language: String = "en-US",
        page: Int
    ): MovieResponse {
        return client.get("movie/top_rated") {
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
        return client.get("movie/$movieId") {
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
        return client.get("movie/$movieId/credits") {
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
        return client.get("movie/$movieId/videos") {
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
    val id: Int = -1,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("poster_path") val posterPath: String? = null,
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
    @SerialName("original_title") val originalTitle: String,
    @SerialName("poster_path") val posterPath: String? = null,
    val overview: String = "",
    val runtime: Int? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("vote_count" )val voteCount: Int? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    val genres: List<Genre>? = null
)


@Serializable
data class CastDto(
    val id: Int,
    val name: String,
    @SerialName("original_name") val originalName: String? = null,
    val character: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    val order: Int? = null,
    @SerialName("credit_id") val creditId: String? = null
)

@Serializable
data class CrewDto(
    val id: Int,
    val name: String,
    @SerialName("original_name") val originalName: String? = null,
    val job: String? = null,
    val department: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("credit_id") val creditId: String? = null
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
    @SerialName("published_at") val publishedAt: String? = null,
    @SerialName("iso_639_1") val iso6391: String? = null,
    @SerialName("iso_3166_1") val iso31661: String? = null
)

@Serializable
data class MovieVideosDto(
    val id: Int,                    // The movie id
    val results: List<VideoDto> = emptyList()
)
