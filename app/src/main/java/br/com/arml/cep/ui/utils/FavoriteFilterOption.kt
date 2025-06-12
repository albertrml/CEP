package br.com.arml.cep.ui.utils

import androidx.compose.runtime.saveable.Saver

sealed class FavoriteFilterOption(val name: String) {
    object None : FavoriteFilterOption("Nenhum")
    object ByCep : FavoriteFilterOption("CEP")
    object ByTitle : FavoriteFilterOption("Título")

    companion object{
        val saver: Saver<FavoriteFilterOption, String> = Saver (
            save = { it.name },
            restore = { name ->
                when(name){
                    None.name -> None
                    ByCep.name -> ByCep
                    ByTitle.name -> ByTitle
                    else -> throw IllegalArgumentException("Unknown FavoriteFilterOption name: $name")
                }
            }
        )
    }
}

val favoriteFilterOptions = listOf(
    FavoriteFilterOption.None,
    FavoriteFilterOption.ByCep,
    FavoriteFilterOption.ByTitle
)