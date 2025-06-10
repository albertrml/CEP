package br.com.arml.cep.ui.utils

import androidx.compose.runtime.saveable.Saver

sealed class LogFilterOption(val name: String) {
    object None : LogFilterOption("Nenhum")
    object ByCep : LogFilterOption("CEP")
    object ByInitialDate : LogFilterOption("A partir de")
    object ByFinalDate : LogFilterOption("Até")
    object ByRangeDate : LogFilterOption("Entre")

    companion object{
        val saver: Saver<LogFilterOption, String> = Saver (
            save = { it.name },
            restore = { name ->
                when(name){
                    None.name -> None
                    ByCep.name -> ByCep
                    ByInitialDate.name -> ByInitialDate
                    ByFinalDate.name -> ByFinalDate
                    ByRangeDate.name -> ByRangeDate
                    else -> throw IllegalArgumentException("Unknown LogFilterOption name: $name")
                }
            }
        )
    }
}

val logFilterOptions = listOf(
    LogFilterOption.None,
    LogFilterOption.ByCep,
    LogFilterOption.ByInitialDate,
    LogFilterOption.ByFinalDate,
    LogFilterOption.ByRangeDate
)