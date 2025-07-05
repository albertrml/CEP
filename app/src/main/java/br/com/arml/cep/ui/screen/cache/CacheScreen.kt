package br.com.arml.cep.ui.screen.cache

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.screen.component.cep.display.DisplayScreen
import br.com.arml.cep.ui.screen.component.cache.CachePlaceAlert
import br.com.arml.cep.ui.screen.component.cache.CachePlaceListComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CacheScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<CacheViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeleteAllAlertShown by rememberSaveable { mutableStateOf(false) }

    val marginScreen = Modifier
        .fillMaxSize()
        .padding(horizontal = MaterialTheme.dimens.mediumMargin)

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
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
                        isDeleteAllAlertShown = true
                    },
                    onDeleteIconClick = { place ->
                        viewModel.onEvent(CacheEvent.OnDelete(place))
                    },
                    onCepFilter = { query ->
                        viewModel.onEvent(CacheEvent.OnFilterByCep(query))
                    },
                    onClearFilter = { viewModel.onEvent(CacheEvent.OnFilterNone) },
                    onNavigateToDetail = { place ->
                        viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(place))
                        isDetailPaneExpanded = true
                        scope.launch {
                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                        }
                    }
                )

                CachePlaceAlert(
                    isVisible = isDeleteAllAlertShown,
                    onChangeVisibility = { isDeleteAllAlertShown = it },
                    onDismissRequest = { isDeleteAllAlertShown = false },
                    onConfirmationRequest = {
                        viewModel.onEvent(CacheEvent.OnDeleteAll)
                        isDeleteAllAlertShown = false
                    }
                )
            }
        },
        detailPane = {
            if (isDetailPaneExpanded) {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    state.placeForDetails?.let {
                        DisplayScreen(
                            modifier = marginScreen,
                            response = Response.Success(it),
                            onBackPress = {
                                scope.launch {
                                    isDetailPaneExpanded = false
                                    navigator.navigateBack()
                                    viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(null))
                                }
                            },
                            onFavoriteClick = { placeEntry ->
                                scope.launch {
                                    viewModel.onEvent(CacheEvent.OnUpdate(placeEntry))
                                    isDetailPaneExpanded = false
                                    navigator.navigateBack()
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}