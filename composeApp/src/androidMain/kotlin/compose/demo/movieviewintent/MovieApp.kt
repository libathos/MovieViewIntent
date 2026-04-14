package compose.demo.movieviewintent

import android.app.Application
import compose.demo.movieviewintent.di.initKoin
import org.koin.android.ext.koin.androidContext

/**
 * Custom Application class.
 * Initialises Koin with Android context before any Activity is created.
 */
class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MovieApp)
        }
    }
}
