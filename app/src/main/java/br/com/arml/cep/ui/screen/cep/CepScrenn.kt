@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package br.com.arml.cep.ui.screen.cep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.ui.screen.display.DisplayScreen
import br.com.arml.cep.ui.screen.search.SearchScreen
import br.com.arml.cep.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun CepScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<CepViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            SearchScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.smallMargin),
                onSearchCep = {
                    scope.launch {
                        viewModel.onEvent(CepEvent.SearchCep(it))
                        navigator.navigateTo(
                            pane = ListDetailPaneScaffoldRole.Detail
                        )
                    }
                }
            )
        },
        detailPane = {
            AnimatedPane {
                DisplayScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.dimens.smallMargin),
                    response = uiState.address,
                    onBackPress = {
                        scope.launch {
                            viewModel.onEvent(CepEvent.ClearCep)
                            navigator.navigateBack()
                        }
                    }
                )
            }
        }
    )
}