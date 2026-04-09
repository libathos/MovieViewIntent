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
                        onLoadTopRated = { vm.loadTopRated() }
                    )
                }
                // Future destinations can be added here, e.g.:
                // composable("details/{movieId}") { backStackEntry ->
                //     val movieId = backStackEntry.arguments?.getString("movieId")
                //     DetailsScreen(movieId = movieId)
                // }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    ListOfMoviesMainComposable()
}