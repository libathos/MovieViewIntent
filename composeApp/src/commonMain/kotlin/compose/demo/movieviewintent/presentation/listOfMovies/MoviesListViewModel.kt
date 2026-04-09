package compose.demo.movieviewintent.presentation.listOfMovies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.demo.movieviewintent.domain.GetTopRatedMoviesUsecase
import compose.demo.movieviewintent.network.MovieDto
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val getTopRated: GetTopRatedMoviesUsecase = GetTopRatedMoviesUsecase()
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val moviesCount: Int? = null,
        val firstTitle: String? = null,
        val showContent: Boolean = false,
        val movies: List<MovieDto> = emptyList(),
    )

    var uiState by mutableStateOf(UiState())
        private set

    fun onShowContent() {
        if (!uiState.showContent) {
            uiState = uiState.copy(showContent = true)
        }
    }

    fun loadTopRated(page: Int = 1) {
        if (uiState.isLoading) return
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                error = null,
                moviesCount = null,
                firstTitle = null,
                movies = emptyList()
            )
            runCatching { getTopRated(page) }
                .onSuccess { resp ->
                    uiState = uiState.copy(
                        isLoading = false,
                        moviesCount = resp.results.size,
                        firstTitle = resp.results.firstOrNull()?.original_title,
                        movies = resp.results
                    )
                }
                .onFailure { t ->
                    uiState = uiState.copy(
                        isLoading = false,
                        error = t.message ?: "Unknown error"
                    )
                    println("[TopRated] Error: $t")
                }
        }
    }
}