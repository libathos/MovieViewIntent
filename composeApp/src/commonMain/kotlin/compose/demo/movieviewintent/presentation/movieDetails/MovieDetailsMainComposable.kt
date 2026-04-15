package compose.demo.movieviewintent.presentation.movieDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import compose.demo.movieviewintent.network.MovieDto
import compose.demo.movieviewintent.presentation.icons.AppIcons
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlin.math.roundToInt

/**
 * High‑level composable for Movie Details screen.
 *
 * It mirrors the provided mock: Top app bar, big poster image, title, metadata row,
 * genre chips, overview section and a bottom primary action button.
 *
 * Uses Material Icons (ArrowBack, PlayArrow, etc.) for cross-platform compatibility.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsMainComposable(
    movie: MovieDto,
    // Optional UI extras not provided by MovieDto
    genres: List<String> = emptyList(),
    durationMinutes: Int? = null, // e.g., 148
    rating: Double? = null,       // e.g., 8.7
    votes: Int? = null,           // e.g., 112000
    releaseDate: String? = null,  // e.g., "Oct 12, 2023"
    director: String? = null,
    cast: List<String> = emptyList(),
    trailerKey: String? = null,   // YouTube video key
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    val appBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = appBarColors,
                title = { Text("Movie Summary") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = AppIcons.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Bottom primary action button similar to screenshot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        if (trailerKey != null) {
                            uriHandler.openUri("https://www.youtube.com/watch?v=$trailerKey")
                        }
                    },
                    enabled = trailerKey != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("WATCH TRAILER")
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = AppIcons.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val posterPath = movie.posterPath
                if (posterPath != null) {
                    val imageUrl = "https://image.tmdb.org/t/p/w780$posterPath"
                    KamelImage(
                        resource = { asyncPainterResource(imageUrl) },
                        contentDescription = movie.originalTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        onFailure = {
                            // Fallback background only
                        }
                    )
                }
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Title
                Text(
                    text = movie.originalTitle,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Subtitle row (genres | duration | ★ rating | votes)
                Spacer(modifier = Modifier.height(6.dp))
                val subtitleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val joinedGenres = if (genres.isNotEmpty()) genres.joinToString(" / ") else null
                    if (!joinedGenres.isNullOrBlank()) {
                        Text(joinedGenres, color = subtitleColor, fontSize = 13.sp)
                        Text("  |  ", color = subtitleColor)
                    }
                    if (durationMinutes != null) {
                        val h = durationMinutes / 60
                        val m = durationMinutes % 60
                        val dur = buildString {
                            if (h > 0) append("${h}h ")
                            append("${m}m")
                        }
                        Text(dur, color = subtitleColor, fontSize = 13.sp)
                        Text("  |  ", color = subtitleColor)
                    }
                    if (rating != null) {
                        Text(
                            "★ ${formatOneDecimal(rating)}",
                            color = subtitleColor,
                            fontSize = 13.sp
                        )
                    }
                    if (votes != null) {
                        Text("  (${formatVotes(votes)})", color = subtitleColor, fontSize = 13.sp)
                    }
                }

                // Genre chips
                if (genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        genres.take(3).forEach { g ->
                            Chip(text = g)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Overview", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Extra details
                if (!director.isNullOrBlank() || cast.isNotEmpty() || !releaseDate.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoLine(label = "Director:", value = director ?: "—")
                    if (cast.isNotEmpty()) {
                        InfoLine(label = "Starring:", value = cast.joinToString())
                    }
                    if (!releaseDate.isNullOrBlank()) {
                        InfoLine(label = "Release:", value = releaseDate)
                    }
                }
            }
        }
    }
}

@Composable
private fun Chip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.width(6.dp))
        Text(text = value)
    }
}

private fun formatVotes(v: Int): String {
    return when {
        v >= 1_000_000 -> ((v / 100_000).toDouble() / 10).toString() + "m" // one decimal
        v >= 1_000 -> ((v / 100).toDouble() / 10).toString() + "k" // one decimal
        else -> v.toString()
    }
}

private fun formatOneDecimal(d: Double): String {
    val scaled = (d * 10).roundToInt() / 10.0
    val asInt = scaled.toInt()
    return if (asInt.toDouble() == scaled) asInt.toString() else scaled.toString()
}

// Small utility to avoid adding ripple/interaction dependencies for text based pseudo icons

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(
            onClick = onClick,
            indication = null,
            role = Role.Button,
            interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource()
        )
    )

// --- Previews ---
@Preview
@Composable
private fun MovieDetailsPreview() {
    val sample = MovieDto(
        originalTitle = "THE ETERNAL PATH",
        posterPath = "/t6ESh4ExAMPLEPoster.jpg", // fake path; image loader will likely fail gracefully
        overview = "In a future where humanity struggles on a dying Earth, an explorer discovers an ancient artifact that offers a chance at redemption. Dr. Elena Vance leads a team through a dangerous rift to an unknown world, uncovering secrets that could change the universe's fate—or destroy it."
    )
    MovieDetailsMainComposable(
        movie = sample,
        genres = listOf("Action", "Sci-Fi", "Adventure"),
        durationMinutes = 148,
        rating = 8.7,
        votes = 112_000,
        releaseDate = "Oct 12, 2023",
        director = "Anna Chen",
        cast = listOf("Eva Green", "Ken Watanabe"),
    )
}
