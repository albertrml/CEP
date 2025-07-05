package br.com.arml.cep.ui.screen.favorite

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
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceDetailsComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceAlert
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceListComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceExtraComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
) {
    val viewmodel = hiltViewModel<FavoriteViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val navigator = rememberListDetailPaneScaffoldNavigator()
    var isUnlikeAlertShown by rememberSaveable { mutableStateOf(false) }
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }
    var isExtraPaneExpanded by rememberSaveable { mutableStateOf(false) }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane(
                enterTransition = paneEnterTransition,
                exitTransition = paneExitTransition
            ) {
                FavoritePlaceListComponent(
                    modifier = modifier.padding(horizontal = MaterialTheme.dimens.mediumMargin),
                    fetchResponse = state.fetchEntries,
                    onFavoriteIconClick = { place ->
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(place))
                        isUnlikeAlertShown = true
                    },
                    onCepFilter = { query ->
                        viewmodel.onEvent(FavoriteEvent.OnFilterByCep(query))
                    },
                    onTitleFilter = { query ->
                        viewmodel.onEvent(FavoriteEvent.OnFilterByTitle(query))
                    },
                    onClearFilter = {
                        viewmodel.onEvent(FavoriteEvent.OnFilterNone)
                    },
                    onNavigateToDetail = {
                        isDetailPaneExpanded = true
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(it))
                        scope.launch {
                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                        }
                    }
                )

                state.placeForUnwanted?.let { place ->
                    FavoritePlaceAlert(
                        place = place,
                        isVisible = isUnlikeAlertShown,
                        onChangeVisibility = { isUnlikeAlertShown = it },
                        onDismissRequest = {
                            viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(null))
                        },
                        onConfirmationRequest = {
                            viewmodel.onEvent(FavoriteEvent.OnClickToUnwanted(place))
                        }
                    )
                }
            }
        },

        detailPane = {
            if (isDetailPaneExpanded) {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    state.placeForEdit?.let {
                        FavoritePlaceDetailsComponent(
                            modifier = modifier
                                .padding(horizontal = MaterialTheme.dimens.smallMargin),
                            placeEntry = it,
                            onNavigateBackToList = {
                                isDetailPaneExpanded = false
                                viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(null))
                                scope.launch { navigator.navigateBack() }
                            },
                            onNavigateToExtra = {
                                isExtraPaneExpanded = true
                                scope.launch { navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Extra) }
                            }
                        )
                    }
                }
            }
        },

        extraPane = {
            AnimatedPane(
                enterTransition = paneEnterTransition,
                exitTransition = paneExitTransition
            ) {
                if (isDetailPaneExpanded == false) isExtraPaneExpanded = false
                if (isExtraPaneExpanded) {
                    state.placeForEdit?.let {
                        FavoritePlaceExtraComponent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = MaterialTheme.dimens.smallMargin),
                            placeEntry = it,
                            onClickToUpdate = { entry ->
                                viewmodel.onEvent(FavoriteEvent.OnUpdateFavorite(entry))
                                isExtraPaneExpanded = false
                                scope.launch { navigator.navigateBack() }
                            },
                            onNavigateBackToDetails = {
                                isExtraPaneExpanded = false
                                scope.launch { navigator.navigateBack() }
                            }
                        )
                    }
                }
            }
        }
    )
}
