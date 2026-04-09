package compose.demo.movieviewintent.presentation.listOfMovies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.demo.movieviewintent.Greeting
import compose.demo.movieviewintent.network.MovieDto

// Stateless UI that renders based on ViewModel state and triggers callbacks
@Composable
fun ListOfMoviesMainContent(
    state: MoviesListViewModel.UiState,
    onShow: () -> Unit,
    onLoadTopRated: () -> Unit,
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                onShow()
                if (!state.isLoading) onLoadTopRated()
            }) {
                Text(if (state.isLoading) "Loading..." else "Load Top Rated")
            }
            AnimatedVisibility(state.showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Compose: $greeting")
                    when {
                        state.error != null -> Text("Error: ${state.error}")
                        state.movies.isNotEmpty() -> MoviesGrid(movies = state.movies)
                        state.isLoading -> Text("Loading...")
                        else -> Text("Press the button to load Top Rated movies")
                    }
                }
            }
        }
    }
}

@Composable
private fun MoviesGrid(movies: List<MovieDto>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MovieCard(title = movie.original_title)
        }
    }
}

@Composable
private fun MovieCard(title: String) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
        ) {
            // Placeholder poster gradient. Replace with real image loader in future.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.tertiaryContainer
                            )
                        )
                    )
            )

            // Title overlay at the bottom similar to screenshot
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Zero-arg wrapper kept for previews and non-Android entry points
@Composable
@Preview
fun ListOfMoviesMainComposable() {
    val vm = remember { MoviesListViewModel() }
    ListOfMoviesMainContent(
        state = vm.uiState,
        onShow = vm::onShowContent,
        onLoadTopRated = { vm.loadTopRated() }
    )
}