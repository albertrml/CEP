package br.com.arml.cep.ui.utils

import androidx.compose.runtime.saveable.Saver

sealed class LogFilterOption(val name: String) {
    data object None : LogFilterOption("Nenhum")
    data object ByCep : LogFilterOption("CEP")
    data object ByInitialDate : LogFilterOption("A partir de")
    data object ByFinalDate : LogFilterOption("Até")
    data object ByRangeDate : LogFilterOption("Entre")

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