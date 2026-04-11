package compose.demo.movieviewintent.domain

import compose.demo.movieviewintent.network.MovieApi
import compose.demo.movieviewintent.network.MovieVideosDto

/**
 * Use case for fetching a movie's videos (trailers, teasers, clips) from TMDB.
 *
 * Mirrors the operator-style API used by other use cases.
 */
class GetMovieVideosUsecase {
    /**
     * Fetch movie videos by id.
     *
     * @param movieId TMDB movie id
     * @param language ISO language (default "en-US")
     * @return [MovieVideosDto] containing the list of videos
     */
    suspend operator fun invoke(
        movieId: Int,
        language: String = "en-US",
    ): MovieVideosDto = MovieApi.getMovieVideos(
        movieId = movieId,
        language = language
    )
}
