package compose.demo.movieviewintent

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainContent
import compose.demo.movieviewintent.presentation.listOfMovies.MoviesListViewModel
import compose.demo.movieviewintent.presentation.movieDetails.MovieDetailsMainComposable
import compose.demo.movieviewintent.presentation.movieDetails.MovieDetailsViewModel

/**
 * Simple sealed hierarchy representing navigation screens.
 */
private sealed interface Screen {
    data object Home : Screen
    data class Details(val movieId: Int) : Screen
}

/**
 * Root composable shared by all platforms.
 * Uses simple state-based navigation – no external navigation library required.
 */
@Composable
fun App() {
    var currentScreen: Screen by remember { mutableStateOf(Screen.Home) }

    AnimatedContent(targetState = currentScreen) { screen ->
        when (screen) {
            is Screen.Home -> {
                val vm: MoviesListViewModel = viewModel { MoviesListViewModel() }
                ListOfMoviesMainContent(
                    state = vm.uiState,
                    onShow = vm::onShowContent,
                    onAction = { action ->
                        when (action) {
                            is MoviesListViewModel.Action.ViewMovieDetails -> {
                                currentScreen = Screen.Details(action.movieId)
                            }
                            else -> vm.onAction(action)
                        }
                    }
                )
            }

            is Screen.Details -> {
                val vm: MovieDetailsViewModel = viewModel(
                    key = "details_${screen.movieId}"
                ) { MovieDetailsViewModel() }

                LaunchedEffect(screen.movieId) {
                    vm.loadMovie(screen.movieId)
                }

                val state = vm.uiState
                when {
                    state.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    state.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${state.error}")
                        }
                    }
                    state.movie != null -> {
                        MovieDetailsMainComposable(
                            movie = state.movie,
                            genres = state.genres,
                            durationMinutes = state.durationMinutes,
                            rating = state.rating,
                            votes = state.votes,
                            releaseDate = state.releaseDate,
                            director = state.director,
                            cast = state.cast,
                            onBack = { currentScreen = Screen.Home },
                        )
                    }
                }
            }
        }
    }
}
