package br.com.arml.cep.ui.screen.favorite

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.ui.screen.component.place.PlaceDetailsComponent
import br.com.arml.cep.ui.screen.component.place.favorite.FavoritePlaceAlert
import br.com.arml.cep.ui.screen.component.place.favorite.FavoritePlaceListComponent
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFetchFavorites
import br.com.arml.cep.ui.theme.dimens
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
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }
    var isUnlikeAlertShown by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewmodel.onEvent(OnFetchFavorites)
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane {
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

                state.placeForUnwanted?.let{ place ->
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
                AnimatedPane {
                    state.placeForEdit?.let {
                        PlaceDetailsComponent(
                            modifier = modifier
                                .padding(horizontal = MaterialTheme.dimens.smallMargin),
                            placeEntry = it,
                            onClickToUpdate = { entry ->
                                viewmodel.onEvent(FavoriteEvent.OnUpdateFavorite(entry))
                                isDetailPaneExpanded = false
                                scope.launch { navigator.navigateBack() }
                            },
                            onNavigateBack = {
                                viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(null))
                                isDetailPaneExpanded = false
                                scope.launch { navigator.navigateBack() }
                            }
                        )
                    }
                }
            }
        }
    )
}
