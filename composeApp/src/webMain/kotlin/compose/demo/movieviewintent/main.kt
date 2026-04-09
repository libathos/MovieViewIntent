package compose.demo.movieviewintent

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainComposable

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        ListOfMoviesMainComposable()
    }
}