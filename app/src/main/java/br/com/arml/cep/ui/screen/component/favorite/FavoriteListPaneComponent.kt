package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.R.string.favoriteListPaneOnFailure_component_testTag
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteListPaneComponentOnFailure
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteListPaneComponentOnSuccess
import br.com.arml.cep.ui.screen.component.favorite.listpane.header.FavoriteListPaneHeader
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun FavoriteListPaneComponent(
    modifier: Modifier = Modifier,
    state: FavoriteState,
    snackbarMsg: String? = null,
    onImportClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onCepFilter: (String) -> Unit = {},
    onTitleFilter: (String) -> Unit = {},
    onNoneFilter: () -> Unit = {},
    onAddNote: (Cep, Note) -> Unit = { _, _ -> },
    onFavoriteIconClick: (Place) -> Unit = {},
    onDeleteNote: (Pair<Cep, Note>) -> Unit = {},
    onNavigateToDetails: (Pair<Address, Note>) -> Unit = {}
) {
    val fetchResponse = state.places
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedFilter by rememberSaveable(stateSaver = PlaceFilterOption.saver) {
        mutableStateOf(PlaceFilterOption.None)
    }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier
            .testTag(stringResource(R.string.favoriteListPaneComponent_component_testTag)),
        topBar = {
            FavoriteListPaneHeader(
                modifier = Modifier
                    .testTag(stringResource(R.string.testTag_favoriteList_header)),
                onImportClick = onImportClick,
                onExportClick = onExportClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            fetchResponse.ShowResults(
                successContent = { places ->
                    FavoriteListPaneComponentOnSuccess(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(vertical = MaterialTheme.dimens.mediumPadding)
                            .testTag(stringResource(R.string.favoriteListPaneOnSuccess_component_testTag)),
                        selectedFilter = selectedFilter,
                        onSelectedFilter = { selectedFilter = it },
                        onCepFilter = onCepFilter,
                        onTitleFilter = onTitleFilter,
                        onNoneFilter = onNoneFilter,
                        places = places,
                        onAddNote = onAddNote,
                        onFavoriteIconClick = onFavoriteIconClick,
                        onDeleteNote = onDeleteNote,
                        onNavigateToDetails = onNavigateToDetails
                    )
                },
                /*loadingContent = {
                    FavoriteListPaneComponentOnSuccess(
                        Modifier
                            .testTag(stringResource(R.string.favoriteListPaneOnLoading_component_testTag))
                    )
                },*/
                failureContent = { exception ->
                    FavoriteListPaneComponentOnFailure(
                        modifier = Modifier
                            .testTag(stringResource(favoriteListPaneOnFailure_component_testTag)),
                        failureMessage = exception.message ?: FetchPlaceException().message
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteDetailPaneComponentOnLoadingPreview() {
    FavoriteListPaneComponent(
        state = FavoriteState(),
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onAddNote = { _, _ -> },
        onFavoriteIconClick = {},
        onDeleteNote = {},
        onNavigateToDetails = {},
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteDetailPaneComponentOnFailurePreview() {
    val response = Response.Failure(FetchPlaceException())
    FavoriteListPaneComponent(
        state = FavoriteState(places = response),
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onAddNote = { _, _ -> },
        onFavoriteIconClick = {},
        onDeleteNote = {},
        onNavigateToDetails = {},
    )
}