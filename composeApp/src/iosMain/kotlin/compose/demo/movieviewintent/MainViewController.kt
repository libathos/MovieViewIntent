package compose.demo.movieviewintent

import androidx.compose.ui.window.ComposeUIViewController
import compose.demo.movieviewintent.presentation.listOfMovies.ListOfMoviesMainComposable

fun MainViewController() = ComposeUIViewController { ListOfMoviesMainComposable() }