package br.com.arml.cep.ui.utils

import androidx.compose.runtime.saveable.Saver

sealed class PlaceFilterOption(val name: String) {
    object None : PlaceFilterOption("Nenhum")
    object ByCep : PlaceFilterOption("CEP")
    object ByTitle : PlaceFilterOption("Título")

    companion object{
        val saver: Saver<PlaceFilterOption, String> = Saver (
            save = { it.name },
            restore = { name ->
                when(name){
                    None.name -> None
                    ByCep.name -> ByCep
                    ByTitle.name -> ByTitle
                    else -> throw IllegalArgumentException("Filtro $name desconhecido")
                }
            }
        )
    }
}

val favoriteFilterOptions = listOf(
    PlaceFilterOption.None,
    PlaceFilterOption.ByCep,
    PlaceFilterOption.ByTitle
)

val cacheFilterOptions = listOf(
    PlaceFilterOption.None,
    PlaceFilterOption.ByCep
)