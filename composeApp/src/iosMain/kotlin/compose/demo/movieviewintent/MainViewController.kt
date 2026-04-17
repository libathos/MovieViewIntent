package compose.demo.movieviewintent

import androidx.compose.ui.window.ComposeUIViewController
import compose.demo.movieviewintent.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }.apply {
        view.backgroundColor = platform.UIKit.UIColor.whiteColor
    }
}
