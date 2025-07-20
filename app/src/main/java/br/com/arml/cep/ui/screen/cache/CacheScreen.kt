package br.com.arml.cep.ui.screen.cache

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.cache.CachePlaceAlert
import br.com.arml.cep.ui.screen.component.cache.CachePlaceListComponent
import br.com.arml.cep.ui.screen.component.search.SearchDetailPane
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CacheScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<CacheViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
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
                CachePlaceListComponent(
                    modifier = marginScreen,
                    fetchResponse = state.fetchEntries,
                    onFavoriteIconClick = { place ->
                        viewModel.onEvent(CacheEvent.OnUpdate(place))
                    },
                    onDeleteCacheClick = {
                        viewModel.onEvent(CacheEvent.OnShowDeleteAllAlert)
                    },
                    onDeleteIconClick = { place ->
                        viewModel.onEvent(CacheEvent.OnDelete(place))
                    },
                    onCepFilter = { query ->
                        viewModel.onEvent(CacheEvent.OnFilterByCep(query))
                    },
                    onClearFilter = { viewModel.onEvent(CacheEvent.OnFilterNone) },
                    onNavigateToDetail = { place ->
                        uiStateHolder.navigateToDetailPane {
                            viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(place))
                        }
                    }
                )

                CachePlaceAlert(
                    isVisible = state.deleteAllAlert,
                    onDismissRequest = {
                        viewModel.onEvent(CacheEvent.OnHideDeleteAllAlert)
                    },
                    onConfirmationRequest = {
                        uiStateHolder.navigateBackToListPane {
                            viewModel.onEvent(CacheEvent.OnDeleteAll)
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
                    state.placeForDetails?.let {
                        SearchDetailPane(
                            modifier = marginScreen,
                            response = Response.Success(it),
                            onBackPress = {
                                uiStateHolder.navigateBackToListPane {
                                    viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(null))
                                }
                            },
                            onFavoriteClick = { placeEntry ->
                                uiStateHolder.navigateBackToListPane {
                                    viewModel.onEvent(CacheEvent.OnUpdate(placeEntry))
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}