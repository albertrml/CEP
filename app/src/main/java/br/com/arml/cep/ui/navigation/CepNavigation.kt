package br.com.arml.cep.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.ui.screen.cep.CepScreen
import br.com.arml.cep.ui.screen.favorite.FavoriteScreen
import br.com.arml.cep.ui.screen.log.LogScreen

@Composable
fun CepNavigation(
    modifier: Modifier = Modifier
){
    var currentDestination: CepDestination by rememberSaveable (stateSaver = CepDestination.Saver) {
        mutableStateOf(CepDestination.SearchDestination)
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            cepDestinations.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(destination.contentDescription)
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                    selected = currentDestination == destination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        currentDestination.SelectDestination(
            onSearch = { CepScreen(modifier = modifier) },
            onHistory = { LogScreen(modifier = modifier) },
            onFavorite = { FavoriteScreen(modifier = modifier) }
        )
    }

}