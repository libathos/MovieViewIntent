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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.demo.movieviewintent.Greeting
import compose.demo.movieviewintent.network.MovieDto
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

// Stateless UI that renders based on ViewModel state and triggers callbacks
@Composable
fun ListOfMoviesMainContent(
    state: MoviesListViewModel.UiState,
    onShow: () -> Unit,
    onAction: (MoviesListViewModel.Action) -> Unit,
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
                if (!state.isLoading) onAction(MoviesListViewModel.Action.LoadTopRated)
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
                        state.movies.isNotEmpty() -> MoviesGrid(movies = state.movies, onAction = onAction)
                        state.isLoading -> Text("Loading...")
                        else -> Text("Press the button to load Top Rated movies")
                    }
                }
            }
        }
    }
}

@Composable
private fun MoviesGrid(
    movies: List<MovieDto>,
    onAction: (MoviesListViewModel.Action) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MovieCard(
                title = movie.original_title,
                posterPath = movie.poster_path,
                onClick = { onAction(MoviesListViewModel.Action.ViewMovieDetails(movie.id)) }
            )
        }
    }
}

@Composable
private fun MovieCard(title: String, posterPath: String?, onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
        ) {
            if (posterPath != null) {
                val imageUrl = "https://image.tmdb.org/t/p/w500$posterPath"
                KamelImage(
                    resource = { asyncPainterResource(data = imageUrl) },
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(16.dp)),
                    onLoading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    onFailure = {
                        PosterPlaceholder()
                    }
                )
            } else {
                PosterPlaceholder()
            }

            // Title overlay at the bottom
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

@Composable
private fun PosterPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
}

// Zero-arg wrapper kept for previews and non-Android entry points
@Composable
@Preview
fun ListOfMoviesMainComposable() {
    val vm = remember { MoviesListViewModel() }
    ListOfMoviesMainContent(
        state = vm.uiState,
        onShow = vm::onShowContent,
        onAction = vm::onAction
    )
}

@Preview
@Composable
private fun MovieCardWithPosterPreview() {
    MaterialTheme {
        Box(modifier = Modifier.width(180.dp)) {
            MovieCard(
                title = "Inception",
                posterPath = "/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg"
            )
        }
    }
}

@Preview
@Composable
private fun MovieCardNoPosterPreview() {
    MaterialTheme {
        Box(modifier = Modifier.width(180.dp)) {
            MovieCard(
                title = "No Poster Available",
                posterPath = null
            )
        }
    }
}

@Preview
@Composable
private fun MovieCardLongTitlePreview() {
    MaterialTheme {
        Box(modifier = Modifier.width(180.dp)) {
            MovieCard(
                title = "The Shawshank Redemption: A Very Long Movie Title That Should Ellipsize",
                posterPath = "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg"
            )
        }
    }
}
