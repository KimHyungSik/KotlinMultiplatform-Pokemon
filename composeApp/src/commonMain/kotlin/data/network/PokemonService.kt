package data.network

import data.model.pokemon.Pokemon
import data.model.pokemon.network.PokemonDto
import data.model.pokemon.network.PokemonList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PokemonService {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
            )
        }
    }

    suspend fun getPokemonList(): PokemonList {
        val response: PokemonList = client.get("https://pokeapi.co/api/v2/pokemon?limit=15").body()
        return response
    }

    suspend fun getPokemonFromUrl(url: String): Pokemon {
        val response: PokemonDto = client.get(url).body()
        return Pokemon(
            name = response.name,
            order = response.order,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${response.order}.png"
        )
    }
}