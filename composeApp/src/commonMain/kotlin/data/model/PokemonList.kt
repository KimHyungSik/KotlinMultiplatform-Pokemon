package data.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonInfoUrl>
)