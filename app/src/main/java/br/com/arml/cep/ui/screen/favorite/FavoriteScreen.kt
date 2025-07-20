package br.com.arml.cep.ui.screen.favorite

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
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceAlert
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceDetailsComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceExtraComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoritePlaceListComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
) {
    val viewmodel = hiltViewModel<FavoriteViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
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
                FavoritePlaceListComponent(
                    modifier = marginScreen,
                    fetchResponse = state.fetchEntries,
                    onFavoriteIconClick = { place ->
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(place))
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
                    onNavigateToDetail = { entry ->
                        uiStateHolder.navigateToDetailPane {
                            viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(entry))
                        }
                    }
                )
                FavoritePlaceAlert(
                    place = state.placeForUnwanted,
                    onDismissRequest = {
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(null))
                    },
                    onConfirmationRequest = {
                        state.placeForUnwanted?.let { place ->
                            if (place == state.placeForEdit){
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(
                                        FavoriteEvent.OnClickToUnwanted(place)
                                    )
                                }
                            } else{
                                viewmodel.onEvent(
                                    FavoriteEvent.OnClickToUnwanted(place)
                                )
                            }
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
                    state.placeForEdit?.let {
                        FavoritePlaceDetailsComponent(
                            modifier = marginScreen,
                            placeEntry = it,
                            onNavigateBackToList = {
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(
                                        FavoriteEvent.OnSelectEntryToEdit(null)
                                    )
                                }
                            },
                            onNavigateToExtra = {
                                uiStateHolder.navigateToExtraPane {
                                    viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(it))
                                }
                            }
                        )
                    }
                }
            }
        },

        extraPane = {
            uiStateHolder.ShowExtraPane {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    state.placeForEdit?.let {
                        FavoritePlaceExtraComponent(
                            modifier = marginScreen,
                            placeEntry = it,
                            onClickToUpdate = { entry ->
                                uiStateHolder.navigateBackToDetailPane {
                                    viewmodel.onEvent(FavoriteEvent.OnUpdateFavorite(entry))
                                }
                            },
                            onNavigateBackToDetails = {
                                uiStateHolder.navigateBackToDetailPane {}
                            }
                        )
                    }
                }
            }
        }
    )
}
