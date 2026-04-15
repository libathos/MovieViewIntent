package compose.demo.movieviewintent.presentation.movieDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.demo.movieviewintent.domain.GetMovieCreditsUsecase
import compose.demo.movieviewintent.domain.GetMovieDetailsUsecase
import compose.demo.movieviewintent.domain.GetMovieVideosUsecase
import compose.demo.movieviewintent.network.MovieDto
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetails: GetMovieDetailsUsecase,
    private val getMovieCredits: GetMovieCreditsUsecase,
    private val getMovieVideos: GetMovieVideosUsecase,
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val movie: MovieDto? = null,
        val genres: List<String> = emptyList(),
        val durationMinutes: Int? = null,
        val rating: Double? = null,
        val votes: Int? = null,
        val releaseDate: String? = null,
        val director: String? = null,
        val cast: List<String> = emptyList(),
        val trailerKey: String? = null,
    )

    var uiState by mutableStateOf(UiState())
        private set

    fun loadMovie(movieId: Int) {
        if (uiState.isLoading) return
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            runCatching {
                val details = getMovieDetails(movieId)
                val credits = getMovieCredits(movieId)
                val videos = getMovieVideos(movieId)
                Triple(details, credits, videos)
            }.onSuccess { (details, credits, videos) ->
                val trailer = videos.results.firstOrNull {
                    it.site.equals("YouTube", ignoreCase = true) &&
                            (it.type.equals("Trailer", ignoreCase = true) || it.type.equals("Teaser", ignoreCase = true))
                }
                uiState = UiState(
                    isLoading = false,
                    movie = MovieDto(
                        id = details.id,
                        originalTitle = details.originalTitle,
                        posterPath = details.posterPath,
                        overview = details.overview
                    ),
                    genres = details.genres?.map { it.name } ?: emptyList(),
                    durationMinutes = details.runtime,
                    rating = details.voteAverage,
                    votes = details.voteCount,
                    releaseDate = details.releaseDate,
                    director = credits.crew.firstOrNull { it.job == "Director" }?.name,
                    cast = credits.cast.take(5).map { it.name },
                    trailerKey = trailer?.key,
                )
            }.onFailure { t ->
                uiState = uiState.copy(
                    isLoading = false,
                    error = t.message ?: "Unknown error"
                )
            }
        }
    }
}


