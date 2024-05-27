import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import data.model.pokemon.Pokemon
import data.model.pokemon.network.PokemonInfoUrl
import data.network.PokemonService
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val pokemonService = PokemonService()
    MaterialTheme {
        var pokemonList by remember { mutableStateOf<List<PokemonInfoUrl>>(emptyList()) }
        LaunchedEffect(Unit) {
            val pokemonListDto = pokemonService.getPokemonList()
            pokemonList = pokemonListDto.results
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pokemonList) { pokemon ->
                    PokemonListItem(
                        callPokemonInfo = {
                            pokemonService.getPokemonFromUrl(pokemon.url)
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun PokemonListItem(
    callPokemonInfo: suspend () -> Pokemon
) {
    var pokemonInfo by remember { mutableStateOf<Pokemon?>(null) }

    LaunchedEffect(Unit) {
        pokemonInfo = callPokemonInfo()
    }

    AsyncImage(
        modifier = Modifier.size(300.dp),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(pokemonInfo?.imageUrl) // 로드할 이미지 url
            .crossfade(true)
            .build(),
        contentDescription = "Compose Multiplatform"
    )
}