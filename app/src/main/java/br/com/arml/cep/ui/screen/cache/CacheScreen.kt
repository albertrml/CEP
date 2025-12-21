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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnAddToFavorite
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDelete
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnDeleteAll
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnFilterNone
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnNavigateToDetailPane
import br.com.arml.cep.ui.screen.cache.CacheEvent.OnNavigateToListPane
import br.com.arml.cep.ui.screen.component.cache.CacheListPaneComponent
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
                CacheListPaneComponent(
                    modifier = marginScreen,
                    fetchResponse = state.places,
                    onDeleteAllCache = {
                        viewModel.onEvent(OnDeleteAll)
                    },
                    onDeletePlace = { place ->
                        viewModel.onEvent(OnDelete(place))
                    },
                    onCepFilter = { query ->
                        viewModel.onEvent(OnFilterByCep(query))
                    },
                    onClearFilter = { viewModel.onEvent(OnFilterNone) },
                    onNavigateToDetail = { place ->
                        uiStateHolder.navigateToDetailPane {
                            viewModel.onEvent(OnNavigateToDetailPane(place))
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
                    state.selectedPlace?.let { selectedPlace ->
                        SearchDetailPane(
                            modifier = marginScreen,
                            response = Response.Success(selectedPlace),
                            onBackPress = {
                                uiStateHolder.navigateBackToListPane {
                                    viewModel.onEvent(OnNavigateToListPane)
                                }
                            },
                            onFavoriteClick = { place ->
                                uiStateHolder.navigateBackToListPane {
                                    viewModel.onEvent(OnAddToFavorite(place))
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}