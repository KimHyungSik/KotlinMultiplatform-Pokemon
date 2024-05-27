package data.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonInfoUrl(
    val name: String,
    val url: String
)