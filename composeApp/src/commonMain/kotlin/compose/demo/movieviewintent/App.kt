package compose.demo.movieviewintent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

import compose.demo.movieviewintent.domain.GetTopRatedMoviesUsecase
import movieviewintent.composeapp.generated.resources.Res
import movieviewintent.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val usecase = remember { GetTopRatedMoviesUsecase() }

        var showContent by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }
        var moviesCount by remember { mutableStateOf<Int?>(null) }
        var firstTitle by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                showContent = true
                if (isLoading) return@Button
                scope.launch {
                    isLoading = true
                    error = null
                    moviesCount = null
                    firstTitle = null
                    try {
                        val response = usecase(page = 1)
                        moviesCount = response.results.size
                        firstTitle = response.results.firstOrNull()?.original_title
                    } catch (t: Throwable) {
                        error = t.message ?: "Unknown error"
                        println("[TopRated] Error: $t")
                    } finally {
                        isLoading = false
                    }
                }
            }) {
                Text(if (isLoading) "Loading..." else "Load Top Rated")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                    when {
                        error != null -> Text("Error: ${'$'}error")
                        moviesCount != null -> {
                            Text("Movies: $moviesCount")
                            if (firstTitle != null) Text("First: $firstTitle")
                        }
                        else -> Text("Press the button to load Top Rated movies")
                    }
                }
            }
        }
    }
}