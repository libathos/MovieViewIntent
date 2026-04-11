package compose.demo.movieviewintent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainComposable
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainContent
import compose.demo.movieviewintent.presentation.listOfMovies.MoviesListViewModel
import compose.demo.movieviewintent.presentation.movieDetails.MovieDetailsMainComposable
import compose.demo.movieviewintent.network.MovieDto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { backStackEntry ->
                    val vm: MoviesListViewModel = viewModel(viewModelStoreOwner = backStackEntry)
                    ListOfMoviesMainContent(
                        state = vm.uiState,
                        onShow = vm::onShowContent,
                        onAction = { action ->
                            when (action) {
                                is MoviesListViewModel.Action.ViewMovieDetails -> {
                                    // In the future, pass movieId as an argument.
                                    navController.navigate("details")
                                }
                                else -> vm.onAction(action)
                            }
                        }
                    )
                }

                composable("details") {
                    // Minimal placeholder movie to render the details screen
                    val placeholder = MovieDto(
                        original_title = "Sample Movie",
                        poster_path = null,
                        overview = "This is a placeholder overview shown for navigation demo."
                    )
                    MovieDetailsMainComposable(
                        movie = placeholder,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    ListOfMoviesMainComposable()
}