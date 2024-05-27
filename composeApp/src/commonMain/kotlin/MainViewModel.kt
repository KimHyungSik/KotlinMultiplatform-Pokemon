import androidx.lifecycle.ViewModel
import data.network.PokemonService

class MainViewModel(
    private val service: PokemonService
): ViewModel() {

    suspend fun getPokemonList() = service.getPokemonList()

    suspend fun getPokemonFromUrl(url: String) = service.getPokemonFromUrl(url)
}