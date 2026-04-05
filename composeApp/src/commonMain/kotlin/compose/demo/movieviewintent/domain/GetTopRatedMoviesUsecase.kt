package compose.demo.movieviewintent.domain

import compose.demo.movieviewintent.network.MovieApi
import compose.demo.movieviewintent.network.MovieResponse

/**
 * Use case for fetching TMDB Top Rated movies.
 *
 * Provides a simple operator-style API so callers can invoke it like a function.
 */
class GetTopRatedMoviesUsecase {
    /**
     * Fetch Top Rated movies.
     *
     * @param apiKey TMDB API key
     * @param page   1-based page index
     * @param language ISO language (default "en-US")
     * @return [MovieResponse] containing page info and list of movies
     */
    suspend operator fun invoke(
        page: Int,
        language: String = "en-US",
    ): MovieResponse = MovieApi.getTopRatedMovies(
        language = language,
        page = page
    )
}
