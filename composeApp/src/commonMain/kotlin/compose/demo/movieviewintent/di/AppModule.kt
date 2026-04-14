package compose.demo.movieviewintent.di

import compose.demo.movieviewintent.domain.GetMovieCreditsUsecase
import compose.demo.movieviewintent.domain.GetMovieDetailsUsecase
import compose.demo.movieviewintent.domain.GetMovieVideosUsecase
import compose.demo.movieviewintent.domain.GetTopRatedMoviesUsecase
import compose.demo.movieviewintent.network.MovieApi
import compose.demo.movieviewintent.network.httpClient
import compose.demo.movieviewintent.presentation.listOfMovies.MoviesListViewModel
import compose.demo.movieviewintent.presentation.movieDetails.MovieDetailsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Shared Koin module that wires all application dependencies.
 *
 * Layers:
 *  - Network: [httpClient] (singleton), [MovieApi] (singleton)
 *  - Domain:  Use cases (factory – lightweight, stateless)
 *  - Presentation: ViewModels (Koin viewModel scope)
 */
val appModule = module {
    // ── Network ──────────────────────────────────────────────
    single { httpClient }
    singleOf(::MovieApi)

    // ── Domain / Use-cases ───────────────────────────────────
    factoryOf(::GetTopRatedMoviesUsecase)
    factoryOf(::GetMovieDetailsUsecase)
    factoryOf(::GetMovieCreditsUsecase)
    factoryOf(::GetMovieVideosUsecase)

    // ── Presentation / ViewModels ────────────────────────────
    viewModelOf(::MoviesListViewModel)
    viewModelOf(::MovieDetailsViewModel)
}

