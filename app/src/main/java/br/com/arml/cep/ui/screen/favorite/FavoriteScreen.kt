package br.com.arml.cep.ui.screen.favorite

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arml.cep.R
import br.com.arml.cep.model.exception.UnknownException.FetchFavoriteException
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.screen.component.log.UnwantedEntryAlert
import br.com.arml.cep.ui.screen.component.place.PlaceDetailsComponent
import br.com.arml.cep.ui.screen.component.place.PlaceFilterComponent
import br.com.arml.cep.ui.screen.component.place.PlaceList
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFetchFavorites
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterByCep
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterByTitle
import br.com.arml.cep.ui.screen.favorite.FavoriteEvent.OnFilterNone
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.FavoriteFilterOption
import br.com.arml.cep.ui.utils.ShowResults
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
) {
    val viewmodel = hiltViewModel<FavoriteViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var selectedFilter by rememberSaveable(stateSaver = FavoriteFilterOption.saver) {
        mutableStateOf<FavoriteFilterOption>(FavoriteFilterOption.None)
    }
    var isAlertVisible by rememberSaveable { mutableStateOf(false) }
    val navigator = rememberListDetailPaneScaffoldNavigator()
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }

    Log.d("FavoriteScreen", "MAIN: ${state.value}")

    LaunchedEffect(Unit) {
        viewmodel.onEvent(OnFetchFavorites)
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane {

                Column(
                    modifier = modifier.padding(horizontal = MaterialTheme.dimens.mediumMargin)
                ) {
                    Header(
                        modifier = modifier,
                        logo = Icons.Filled.Favorite,
                        title = stringResource(R.string.favorite_title),
                        colorLogo = Color.Red
                    )
                    PlaceFilterComponent(
                        modifier = Modifier.fillMaxWidth(),
                        selectedFilter = selectedFilter,
                        onFilterByCep = { query -> viewmodel.onEvent(OnFilterByCep(query)) },
                        onFilterByTitle = { query -> viewmodel.onEvent(OnFilterByTitle(query)) },
                        onNoneFilter = { viewmodel.onEvent(OnFilterNone) },
                        onChangeFilter = { filter -> selectedFilter = filter }
                    )
                    Box(
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.dimens.smallPadding)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        state.value.fetchEntries.ShowResults(
                            successContent = { placeList ->
                                PlaceList(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .fillMaxSize(),
                                    placeEntries = placeList,
                                    logo = Icons.Filled.Favorite,
                                    colorLogo = Color.Red,
                                    onClickLogo = { place ->
                                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(place))
                                        isAlertVisible = true
                                    },
                                    onClickEntry = { place ->
                                        scope.launch {
                                            isDetailPaneExpanded = true
                                            viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(place))
                                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                                        }
                                    }
                                )
                            },
                            loadingContent = {
                                CircularProgressIndicator()
                            },
                            failureContent = {
                                Text(
                                    text = it.message?: FetchFavoriteException().message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        )
                    }
                }
            }

            state.value.placeForUnwanted?.let { place ->
                UnwantedEntryAlert(
                    showDialog = isAlertVisible,
                    entry = place,
                    onDismissRequest = {
                        isAlertVisible = false
                        viewmodel.onEvent(FavoriteEvent.OnSelectEntryToUnwanted(null))
                    },
                    onConfirmation = {
                        viewmodel.onEvent(FavoriteEvent.OnClickToUnwanted(place))
                        isAlertVisible = false
                    }
                )
            }

        },

        detailPane = {
            if (isDetailPaneExpanded) {
                AnimatedPane {
                    state.value.placeForEdit?.let {
                        PlaceDetailsComponent(
                            modifier = modifier
                                .padding(horizontal = MaterialTheme.dimens.smallMargin),
                            placeEntry = it,
                            onClickToUpdate = { entry ->
                                viewmodel.onEvent(FavoriteEvent.OnUpdateFavorite(entry))
                            },
                            onNavigateBack = {
                                scope.launch {
                                    isDetailPaneExpanded = false
                                    viewmodel.onEvent(FavoriteEvent.OnSelectEntryToEdit(null))
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
