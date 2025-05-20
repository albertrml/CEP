package br.com.arml.cep.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.arml.cep.ui.screen.search.SearchScreen

@Composable
fun CepNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Search.route
    ){
        composable(route = Search.route){
            SearchScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}