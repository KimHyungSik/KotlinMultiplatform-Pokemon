package data.model.pokemon.network

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val name: String,
    val order: Int,
)