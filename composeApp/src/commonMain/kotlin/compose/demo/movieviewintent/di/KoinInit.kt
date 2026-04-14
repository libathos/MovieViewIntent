package compose.demo.movieviewintent.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform

/**
 * Initialises Koin with the shared [appModule].
 *
 * Each platform entry-point calls this once **before** composition starts.
 * An optional [platformConfiguration] lambda lets platform-specific code add extra
 * modules or Android context, etc.
 *
 * Safe to call multiple times – subsequent calls are silently ignored.
 */
fun initKoin(platformConfiguration: KoinApplication.() -> Unit = {}) {
    // Guard against double-init (e.g. hot-reload, or both platform + App() calling it)
    try {
        KoinPlatform.getKoin()
        return // already initialised
    } catch (_: Exception) {
        // Koin not started yet – continue
    }
    startKoin {
        platformConfiguration()
        modules(appModule)
    }
}

