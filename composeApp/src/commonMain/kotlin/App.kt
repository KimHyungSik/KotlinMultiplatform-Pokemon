import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import data.model.pokemon.Pokemon
import data.model.pokemon.network.PokemonInfoUrl
import data.network.PokemonService
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(navController)
                }
                composable(
                    route = "detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) {
                    PokemonInfoScreen(
                        navController = navController,
                        it.arguments?.getInt("id")!!
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel<MainViewModel>()
) {
    var pokemonList by remember { mutableStateOf<List<PokemonInfoUrl>>(emptyList()) }
    LaunchedEffect(Unit) {
        val pokemonListDto = viewModel.getPokemonList()
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
                        viewModel.getPokemonFromUrl(pokemon.url)
                    },
                    openDetail = {
                        navController.navigate("detail/$it")
                    }
                )
            }
        }

    }
}

@Composable
fun PokemonListItem(
    callPokemonInfo: suspend () -> Pokemon,
    openDetail: (Int) -> Unit,
) {
    var pokemonInfo by remember { mutableStateOf<Pokemon?>(null) }

    LaunchedEffect(Unit) {
        pokemonInfo = callPokemonInfo()
    }

    AsyncImage(
        modifier = Modifier.size(300.dp).clickable { pokemonInfo?.order?.let { openDetail(it) } },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(pokemonInfo?.imageUrl) // 로드할 이미지 url
            .crossfade(true)
            .build(),
        contentDescription = "Compose Multiplatform"
    )
}

@Composable
fun PokemonInfoScreen(
    navController: NavHostController,
    pokemonId: Int
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AsyncImage(
                modifier = Modifier.size(300.dp).clickable {
                    navController.popBackStack()
                },
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png")
                    .crossfade(true)
                    .build(),
                contentDescription = "Compose Multiplatform"
            )
        }
    }

}

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}