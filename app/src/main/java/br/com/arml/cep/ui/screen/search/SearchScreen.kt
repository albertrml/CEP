package br.com.arml.cep.ui.screen.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.search.SearchDetailPane
import br.com.arml.cep.ui.screen.component.search.SearchListPane
import br.com.arml.cep.ui.screen.search.SearchEvent.OnClear
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavorite
import br.com.arml.cep.ui.screen.search.SearchEvent.OnSearch
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val uiStateHolder = rememberNavigableListDetailPaneScaffoldStateHolder()

    val marginScreen = Modifier
        .fillMaxSize()
        .padding(horizontal = MaterialTheme.dimens.mediumMargin)

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = uiStateHolder.navigator,
        listPane = {
            AnimatedPane(
                enterTransition = paneEnterTransition,
                exitTransition = paneExitTransition
            ) {
                SearchListPane(
                    modifier = marginScreen,
                    onSearchCep = { query ->
                        uiStateHolder.navigateToDetailPane {
                            viewModel.onEvent(OnSearch(query))
                        }
                    }
                )
            }
        },
        detailPane = {
            uiStateHolder.ShowDetailPane {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    SearchDetailPane(
                        modifier = marginScreen,
                        response = uiState.entry,
                        onBackPress = {
                            uiStateHolder.navigateBackToListPane {
                                viewModel.onEvent(OnClear)
                            }
                        },
                        onFavoriteClick = { placeEntry ->
                            uiStateHolder.navigateBackToListPane {
                                viewModel.onEvent(OnFavorite(placeEntry))
                            }
                        }
                    )
                }
            }
        }
    )
}