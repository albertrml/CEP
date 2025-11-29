package br.com.arml.cep.ui.screen.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.favorite.FavoriteDetailPaneComponent
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteChangeAlert
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteExport
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteImport
import br.com.arml.cep.ui.screen.component.favorite.FavoriteListPaneComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.exportBackupLauncher
import br.com.arml.cep.ui.utils.getExportIntent
import br.com.arml.cep.ui.utils.getImportIntent
import br.com.arml.cep.ui.utils.importBackupLauncher
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen(modifier: Modifier = Modifier) {
    val viewmodel = hiltViewModel<FavoriteViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    var snackBarMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val uiStateHolder = rememberNavigableListDetailPaneScaffoldStateHolder()

    val launcherExportBackup = exportBackupLauncher(
        context = LocalContext.current,
        json = (state.exportedFavorites as? Response.Success<String>)?.result ?: "",
        onExportRequest = { viewmodel.onEvent(FavoriteEvent.OnConfirmExport) }
    )

    val launcherImportBackup = importBackupLauncher(
        context = LocalContext.current,
        onSuccess = { json -> viewmodel.onEvent(FavoriteEvent.OnConfirmImport(json)) }
    )

    LaunchedEffect(Unit) {
        viewmodel.effect.collect { effect ->
            when(effect){
                is FavoriteEffect.ShowSnackbar -> { snackBarMessage = effect.message }
            }
        }
    }

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
                FavoriteListPaneComponent(
                    modifier = marginScreen,
                    state = state,
                    snackbarMsg = snackBarMessage,
                    onExportClick = { viewmodel.onEvent(FavoriteEvent.OnExportFavorites) },
                    onImportClick = { viewmodel.onEvent(FavoriteEvent.OnImportFavorites) },
                    onCepFilter = { query ->
                        viewmodel.onEvent(FavoriteEvent.OnFilterByCep(query))
                    },
                    onTitleFilter = { query ->
                        viewmodel.onEvent(FavoriteEvent.OnFilterByTitle(query))
                    },
                    onNoneFilter = { viewmodel.onEvent(FavoriteEvent.OnFilterNone) },
                    onFavoriteIconClick = { place ->
                        viewmodel.onEvent(FavoriteEvent.OnSelectFavoriteToUnwanted(place))
                    },
                    onAddNote = { cep, note ->
                        viewmodel.onEvent(FavoriteEvent.OnAddNoteToFavorite(cep, note))
                    },
                    onDeleteNote = { noteWithCep ->
                        viewmodel.onEvent(FavoriteEvent.OnDeleteNoteFromFavorite(noteWithCep))
                    },
                    onNavigateToDetails = { favorite ->
                        uiStateHolder.navigateToDetailPane {
                            viewmodel.onEvent(
                                FavoriteEvent.OnNavigateToDetailPane(
                                    favorite.second,
                                    favorite.first
                                )
                            )
                        }
                    }
                )
                FavoriteChangeAlert(
                    place = state.selectedFavoriteToUnwanted,
                    onDismissRequest = {
                        viewmodel.onEvent(FavoriteEvent.OnCancelFavoriteToUnwanted)
                    },
                    onConfirmationRequest = {
                        state.selectedFavoriteToUnwanted?.let { place ->
                            val event = FavoriteEvent.OnConfirmFavoriteToUnwanted(place)
                            if (place.address == state.selectedDataToDetail?.first) {
                                uiStateHolder.navigateBackToListPane { viewmodel.onEvent(event) }
                            } else { viewmodel.onEvent(event) }
                        }
                    }
                )

                FavoriteImport(
                    isVisibility = state.isVisibleImportAlert,
                    onDismissRequest = { viewmodel.onEvent(FavoriteEvent.OnCancelImport) },
                    onConfirmationRequest = { launcherImportBackup.launch(getImportIntent()) }
                )
                FavoriteExport(
                    isVisibility = state.isVisibleExportAlert,
                    onDismissRequest = { viewmodel.onEvent(FavoriteEvent.OnCancelExport) },
                    onConfirmationRequest = { launcherExportBackup.launch(getExportIntent()) }
                )
            }
        },

        detailPane = {
            uiStateHolder.ShowDetailPane {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    state.selectedDataToDetail?.let { favorite ->
                        FavoriteDetailPaneComponent(
                            modifier = marginScreen,
                            favorite = favorite,
                            onNavigateBackToList = {
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent( FavoriteEvent.OnNavigateBackToListPane)
                                }
                            },
                            onEditNote = { note ->
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(FavoriteEvent.OnUpdateNoteFromFavorite(note))
                                }
                            },
                            onCreateNote = { cep, note ->
                                uiStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(
                                        FavoriteEvent.OnAddNoteToFavorite(cep, note)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}