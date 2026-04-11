package compose.demo.movieviewintent.domain

import compose.demo.movieviewintent.network.MovieApi
import compose.demo.movieviewintent.network.MovieCreditsDto

/**
 * Use case for fetching a movie's credits (cast & crew) from TMDB.
 *
 * Mirrors the style of other use cases by providing an operator-style API.
 */
class GetMovieCreditsUsecase {
    /**
     * Fetch movie credits by id.
     *
     * @param movieId TMDB movie id
     * @param language ISO language (default "en-US")
     * @return [MovieCreditsDto] containing cast and crew lists
     */
    suspend operator fun invoke(
        movieId: Int,
        language: String = "en-US",
    ): MovieCreditsDto = MovieApi.getMovieCredits(
        movieId = movieId,
        language = language
    )
}
