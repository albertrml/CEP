package br.com.arml.cep.ui.screen.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import br.com.arml.core.response.Response
import br.com.arml.cep.ui.navigation.rememberNavigableListDetailPaneScaffoldStateHolder
import br.com.arml.cep.ui.screen.component.search.AddressSearchDetailPane
import br.com.arml.cep.ui.screen.component.search.CepSearchDetailPane
import br.com.arml.cep.ui.screen.component.search.SearchListPane
import br.com.arml.cep.ui.screen.component.search.listpane.SearchTab
import br.com.arml.cep.ui.screen.component.search.listpane.SearchTabSaver
import br.com.arml.cep.ui.screen.search.SearchEvent.OnClear
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavorite
import br.com.arml.cep.ui.screen.search.SearchEvent.OnCepSearch
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.paneEnterTransition
import br.com.arml.cep.ui.utils.paneExitTransition

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val uiStateHolder = rememberNavigableListDetailPaneScaffoldStateHolder()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context)
                    )
                }
            }
        }
    }

    var selectedTab by rememberSaveable(stateSaver = SearchTabSaver) {
        mutableStateOf(SearchTab.CEP)
    }

    val marginScreen = Modifier
        .fillMaxSize()
        .padding(horizontal = MaterialTheme.dimens.mediumMargin)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        NavigableListDetailPaneScaffold(
            modifier = Modifier.padding(padding),
            navigator = uiStateHolder.navigator,
            listPane = {
                AnimatedPane(
                    enterTransition = paneEnterTransition,
                    exitTransition = paneExitTransition
                ) {
                    SearchListPane(
                        modifier = marginScreen,
                        selectedTab = selectedTab,
                        onChangeTab = { selectedTab = it },
                        onSearchCep = { query ->
                            uiStateHolder.navigateToDetailPane {
                                viewModel.onEvent(OnCepSearch(query))
                            }
                        },
                        onSearchAddress = { uf, city, street ->
                            uiStateHolder.navigateToDetailPane {
                                viewModel.onEvent(SearchEvent.OnAddressSearch(uf, city, street))
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
                        when (selectedTab) {
                            is SearchTab.Address -> {
                                AddressSearchDetailPane(
                                    modifier = marginScreen,
                                    response = uiState.addressSearchResponse,
                                    onBackPress = {
                                        uiStateHolder.navigateBackToListPane {
                                            viewModel.onEvent(OnClear)
                                        }
                                    },
                                    onNavigateToAddress = { placeEntry ->
                                        uiStateHolder.navigateToExtraPane {
                                            viewModel.onEvent(SearchEvent.OnNavigateToExtraPane(placeEntry))
                                        }
                                    }
                                )
                            }

                            is SearchTab.CEP -> {
                                CepSearchDetailPane(
                                    modifier = marginScreen,
                                    response = uiState.cepSearchResponse,
                                    onBackPress = {
                                        uiStateHolder.navigateBackToListPane {
                                            viewModel.onEvent(OnClear)
                                        }
                                    },
                                    onFavoriteClick = { placeEntry ->
                                        uiStateHolder.navigateBackToListPane {
                                            viewModel.onEvent(OnFavorite(placeEntry))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            },
            extraPane = {
                uiState.selectedPlace?.let { placeEntry ->
                    uiStateHolder.ShowExtraPane {
                        AnimatedPane(
                            enterTransition = paneEnterTransition,
                            exitTransition = paneExitTransition
                        ) {
                            CepSearchDetailPane(
                                modifier = marginScreen,
                                response = Response.Success(placeEntry),
                                onBackPress = {
                                    uiStateHolder.navigateBackToDetailPane {
                                        viewModel.onEvent(SearchEvent.OnNavigateBackToDetailPane)
                                    }
                                },
                                onFavoriteClick = { placeEntry ->
                                    uiStateHolder.navigateBackToDetailPane {
                                        viewModel.onEvent(OnFavorite(placeEntry))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}