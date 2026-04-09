package compose.demo.movieviewintent

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainComposable

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MovieViewIntent",
    ) {
        ListOfMoviesMainComposable()
    }
}