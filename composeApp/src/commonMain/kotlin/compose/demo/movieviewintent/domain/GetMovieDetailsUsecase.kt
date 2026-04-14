package compose.demo.movieviewintent.domain

import compose.demo.movieviewintent.network.MovieApi
import compose.demo.movieviewintent.network.MovieDetailsDto

/**
 * Use case for fetching a single movie's details from TMDB.
 *
 * Mirrors the style of [GetTopRatedMoviesUsecase] by exposing an operator-style API
 * so callers can simply invoke it like a function.
 */
class GetMovieDetailsUsecase(private val movieApi: MovieApi) {
    /**
     * Fetch movie details by id.
     *
     * @param movieId TMDB movie id
     * @param language ISO language (default "en-US")
     * @return [MovieDetailsDto] movie details payload
     */
    suspend operator fun invoke(
        movieId: Int,
        language: String = "en-US",
    ): MovieDetailsDto = movieApi.getMovieDetails(
        movieId = movieId,
        language = language
    )
}
