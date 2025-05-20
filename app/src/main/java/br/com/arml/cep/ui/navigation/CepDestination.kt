package br.com.arml.cep.ui.navigation

interface CepNavigation {
    val route: String
}

object Search : CepNavigation {
    override val route: String = "search_screen"
}