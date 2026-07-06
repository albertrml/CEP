package br.com.arml.cep.ui.screen.favorite

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.favorite.FavoriteDetailPaneComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoriteListPaneComponent
import br.com.arml.cep.ui.screen.component.favorite.dialog.FavoriteChangeAlert
import br.com.arml.cep.ui.screen.component.favorite.dialog.FavoriteExport
import br.com.arml.cep.ui.screen.component.favorite.dialog.FavoriteImport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnAddNoteToFavorite
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnCancelFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmExport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmFavoriteToUnwanted
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnConfirmImport
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnDeleteNoteFromFavorite
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnExportFavorites
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterByTitle
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterNone
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnImportFavorites
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnNavigateToDetailPane
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnSelectFavoriteToUnwanted
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.UiText
import br.com.arml.cep.ui.utils.exportBackupLauncher
import br.com.arml.cep.ui.utils.getExportIntent
import br.com.arml.cep.ui.utils.getImportIntent
import br.com.arml.cep.ui.utils.importBackupLauncher
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen(modifier: Modifier = Modifier) {
    val viewmodel = hiltViewModel<FavoriteViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    var snackBarMessage by remember { mutableStateOf<UiText?>(null) }
    val paneScaffoldStateHolder = rememberNavigableListDetailPaneScaffoldStateHolder()

    var pendingExportJson by rememberSaveable { mutableStateOf("") }

    val launcherExportBackup = exportBackupLauncher(
        context = LocalContext.current,
        json = pendingExportJson,
    )

    val launcherImportBackup = importBackupLauncher(
        context = LocalContext.current,
        onSuccess = { json -> viewmodel.onEvent(OnConfirmImport(json)) }
    )

    LaunchedEffect(Unit) {
        viewmodel.effect.collectLatest { effect ->
            Log.d("FavoriteScreen", "LaunchedEffect: $effect")
            when(effect){
                is FavoriteEffect.ShowSnackbar -> { snackBarMessage = effect.message }
                is FavoriteEffect.OnSuccessExportFavorites -> {
                    pendingExportJson = effect.message
                    launcherExportBackup.launch(getExportIntent())
                }
            }
        }
    }

    val marginScreen = modifier
        .fillMaxSize()
        .padding(horizontal = MaterialTheme.dimens.mediumMargin)

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = paneScaffoldStateHolder.navigator,
        listPane = {
            AnimatedPane(
                enterTransition = paneEnterTransition,
                exitTransition = paneExitTransition
            ) {
                FavoriteListPaneComponent(
                    modifier = marginScreen,
                    state = state,
                    snackbarMsg = snackBarMessage,
                    onExportClick = { viewmodel.onEvent(OnExportFavorites) },
                    onImportClick = { viewmodel.onEvent(OnImportFavorites) },
                    onCepFilter = { query ->
                        viewmodel.onEvent(OnFilterByCep(query))
                    },
                    onTitleFilter = { query ->
                        viewmodel.onEvent(OnFilterByTitle(query))
                    },
                    onNoneFilter = { viewmodel.onEvent(OnFilterNone) },
                    onFavoriteIconClick = { place ->
                        viewmodel.onEvent(OnSelectFavoriteToUnwanted(place))
                    },
                    onAddNote = { cep, note ->
                        viewmodel.onEvent(OnAddNoteToFavorite(cep, note))
                    },
                    onDeleteNote = { noteWithCep ->
                        viewmodel.onEvent(OnDeleteNoteFromFavorite(noteWithCep))
                    },
                    onNavigateToDetails = { favorite ->
                        val (address,note) = favorite
                        paneScaffoldStateHolder.navigateToDetailPane {
                            viewmodel.onEvent(OnNavigateToDetailPane(address, note))
                        }
                    }
                )
                FavoriteChangeAlert(
                    place = state.selectedFavoriteToUnwanted,
                    onDismissRequest = { viewmodel.onEvent(OnCancelFavoriteToUnwanted) },
                    onConfirmationRequest = {
                        state.selectedFavoriteToUnwanted?.let { place ->
                            val event = OnConfirmFavoriteToUnwanted(place)
                            if (place.address == state.selectedDataToDetail?.first) {
                                paneScaffoldStateHolder.navigateBackToListPane { viewmodel.onEvent(event) }
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
                    onConfirmationRequest = { viewmodel.onEvent(OnConfirmExport) }
                )
            }
        },

        detailPane = {
            paneScaffoldStateHolder.ShowDetailPane {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    state.selectedDataToDetail?.let { favorite ->
                        FavoriteDetailPaneComponent(
                            modifier = marginScreen,
                            favorite = favorite,
                            onNavigateBackToList = {
                                paneScaffoldStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent( FavoriteEvent.OnNavigateBackToListPane)
                                }
                            },
                            onEditNote = { note ->
                                paneScaffoldStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(FavoriteEvent.OnUpdateNoteFromFavorite(note))
                                }
                            },
                            onCreateNote = { cep, note ->
                                paneScaffoldStateHolder.navigateBackToListPane {
                                    viewmodel.onEvent(
                                        OnAddNoteToFavorite(cep, note)
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