import data.network.PokemonService
import org.koin.dsl.module

val appModule = module {
    single { PokemonService() }
}