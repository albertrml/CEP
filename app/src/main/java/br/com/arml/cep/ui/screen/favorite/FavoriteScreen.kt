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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.favorite.FavoriteChangeAlert
import br.com.arml.cep.ui.screen.component.favorite.FavoriteDetailsComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoriteExport
import br.com.arml.cep.ui.screen.component.favorite.FavoriteExtraComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoriteImport
import br.com.arml.cep.ui.screen.component.favorite.FavoriteListComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.exportBackupLauncher
import br.com.arml.cep.ui.utils.getExportIntent
import br.com.arml.cep.ui.utils.getImportIntent
import br.com.arml.cep.ui.utils.importBackupLauncher
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

    val launcherExportBackup = exportBackupLauncher(
        context = LocalContext.current,
        json = (state.exportBackup as? Response.Success<String>)?.result ?: "",
        onExportRequest = {
            viewmodel.onEvent(FavoriteEvent.OnExportHide)
        }
    )

    val launcherImportBackup = importBackupLauncher(
        context = LocalContext.current,
        onSuccess = { json ->
            viewmodel.onEvent(FavoriteEvent.OnImportBackup(json))
        }
    )

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
                FavoriteListComponent(
                    modifier = marginScreen,
                    fetchResponse = state.fetchEntries,
                    onExportClick = {
                        viewmodel.onEvent(FavoriteEvent.OnExportShow)
                    },
                    onImportClick = {
                        viewmodel.onEvent(FavoriteEvent.OnImportShow)
                    },
                    onFavoriteIconClick = { place ->
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(place))
                    },
                    onCepFilter = { query ->
                        viewmodel.onEvent(FavoriteEvent.OnFilterByCep(query))
                    },
                    onTitleFilter = { query ->
                        viewmodel.onEvent(FavoriteEvent.OnFilterByTitle(query))
                    },
                    onNoneFilter = {
                        viewmodel.onEvent(FavoriteEvent.OnFilterNone)
                    },
                    onNavigateToDetails = { entry ->
                        uiStateHolder.navigateToDetailPane {
                            viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(entry))
                        }
                    }
                )
                FavoriteChangeAlert(
                    place = state.placeForUnwanted,
                    onDismissRequest = {
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(null))
                    },
                    onConfirmationRequest = {
                        state.placeForUnwanted?.let { place ->
                            if (place == state.placeForEdit) {
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(
                                        FavoriteEvent.OnClickToUnwanted(place)
                                    )
                                }
                            } else {
                                viewmodel.onEvent(
                                    FavoriteEvent.OnClickToUnwanted(place)
                                )
                            }
                        }
                    }
                )

                FavoriteImport(
                    isVisibility = state.importAlert,
                    onDismissRequest = {
                        viewmodel.onEvent(FavoriteEvent.OnImportHide)
                    },
                    onConfirmationRequest = {
                        launcherImportBackup.launch(getImportIntent())
                    }
                )
                FavoriteExport(
                    isVisibility = state.exportAlert,
                    onDismissRequest = {
                        viewmodel.onEvent(FavoriteEvent.OnExportHide)
                    },
                    onConfirmationRequest = {
                        viewmodel.onEvent(FavoriteEvent.OnExportFavorites)
                        launcherExportBackup.launch(getExportIntent())
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
                    state.placeForEdit?.let { placeEntry ->
                        FavoriteDetailsComponent(
                            modifier = marginScreen,
                            placeEntry = placeEntry,
                            onNavigateBackToList = {
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(
                                        FavoriteEvent.OnSelectEntryToEdit(null)
                                    )
                                }
                            },
                            onNavigateToExtra = {
                                uiStateHolder.navigateToExtraPane {
                                    viewmodel.onEvent(
                                        FavoriteEvent.OnSelectEntryToEdit(placeEntry)
                                    )
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
                    state.placeForEdit?.let { placeEntry ->
                        FavoriteExtraComponent(
                            modifier = marginScreen,
                            placeEntry = placeEntry,
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