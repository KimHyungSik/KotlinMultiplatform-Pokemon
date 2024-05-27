package data.model.pokemon.network

import kotlinx.serialization.Serializable

@Serializable
data class PokemonInfoUrl(
    val name: String,
    val url: String
)