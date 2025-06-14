@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package br.com.arml.cep.ui.screen.cache

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.ui.screen.component.cep.display.DisplayScreen
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog
import br.com.arml.cep.ui.screen.component.place.PlaceList
import br.com.arml.cep.ui.screen.component.place.UnwantedOptionComponent
import br.com.arml.cep.ui.utils.ShowResults
import kotlinx.coroutines.launch

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


    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UnwantedOptionComponent(
                        onFilterByCep = { viewModel.onEvent(CacheEvent.OnFilterByCep(it)) },
                        onNoneFilter = { viewModel.onEvent(CacheEvent.OnFilterNone) },
                        onClickToShowAlert = { isDeleteAllAlertShown = true }
                    )

                    state.fetchEntries.ShowResults(
                        successContent = { placeList ->
                            PlaceList(
                                modifier = Modifier,
                                placeEntries = placeList,
                                favoriteIcon = Icons.Default.FavoriteBorder,
                                colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
                                onFavoriteIconClick = { place ->
                                    viewModel.onEvent(CacheEvent.OnDelete(place))
                                },
                                onClickLogo = { place ->
                                    viewModel.onEvent(CacheEvent.OnUpdate(place))
                                },
                                onClickEntry = { place ->
                                    viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(place))
                                    scope.launch {
                                        isDetailPaneExpanded = true
                                        navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                                    }
                                }
                            )
                        }
                    )

                    if (isDeleteAllAlertShown) {
                        CepAlertDialog(
                            dialogText = "Delete All",
                            dialogTitle = "Are you sure you want to delete all unwanted places?",
                            onDismissRequest = { isDeleteAllAlertShown = false },
                            onConfirmationRequest = {
                                viewModel.onEvent(CacheEvent.OnDeleteAll)
                                isDeleteAllAlertShown = false
                            },
                        )
                    }
                }
            }
        },
        detailPane = {
            if (isDetailPaneExpanded) {
                AnimatedPane {
                    state.placeForDetails?.let {
                        DisplayScreen(
                            response = Response.Success<PlaceEntry>(it),
                            onBackPress = {
                                scope.launch {
                                    isDetailPaneExpanded = false
                                    navigator.navigateBack()
                                    viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(null))
                                }
                            },
                            onFavoriteClick = {
                                scope.launch {
                                    viewModel.onEvent(CacheEvent.OnUpdate(it))
                                    viewModel.onEvent(CacheEvent.OnSelectEntryForDetails(null))
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}