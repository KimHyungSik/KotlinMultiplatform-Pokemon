package data.network

import data.model.PokemonList
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

    suspend fun getPokemonList() {
        val response: PokemonList = client.get("https://pokeapi.co/api/v2/pokemon?limit=15").body()
        println("LOGEE : $response")
    }
}