package compose.demo.movieviewintent

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainComposable

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MovieViewIntent",
        icon = painterResource("mvi_app_icon.png"),
    ) {
        ListOfMoviesMainComposable()
    }
}