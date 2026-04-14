package compose.demo.movieviewintent

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.demo.movieviewintent.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MovieViewIntent",
            icon = painterResource("mvi_app_icon.png"),
        ) {
            App()
        }
    }
}

