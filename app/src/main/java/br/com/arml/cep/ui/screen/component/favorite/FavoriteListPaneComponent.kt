package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteFilter
import br.com.arml.cep.ui.screen.component.favorite.listpane.FavoriteListComponent
import br.com.arml.cep.ui.screen.component.favorite.listpane.header.FavoriteListPaneHeader
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun FavoriteListPaneComponent(
    modifier: Modifier = Modifier,
    state: FavoriteState,
    snackbarMsg: String? = null,
    onImportClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onCepFilter: (String) -> Unit,
    onTitleFilter: (String) -> Unit,
    onNoneFilter: () -> Unit,
    onAddNote: (Cep, Note) -> Unit,
    onFavoriteIconClick: (Place) -> Unit,
    onDeleteNote: (Pair<Cep, Note>) -> Unit,
    onNavigateToDetails: (Pair<Address, Note>) -> Unit
) {
    val fetchResponse = state.fetchEntries
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier,
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
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(vertical = MaterialTheme.dimens.mediumPadding),
                        verticalArrangement = Arrangement
                            .spacedBy(MaterialTheme.dimens.mediumPadding)
                    ) {
                        FavoriteFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(stringResource(R.string.testTag_favoriteList_filter)),
                            onFilterByCep = { query -> onCepFilter(query) },
                            onFilterByTitle = { query -> onTitleFilter(query) },
                            onNoneFilter = { onNoneFilter() }
                        )

                        FavoriteListComponent(
                            modifier = Modifier
                                //.align(Alignment.TopCenter)
                                .testTag(stringResource(R.string.testTag_favoriteList_onSuccess)),
                            places = places,
                            onAddNote = onAddNote,
                            onFavoriteIconClick = onFavoriteIconClick,
                            onDeleteNote = onDeleteNote,
                            onNavigateToDetail = onNavigateToDetails
                        )
                    }
                },
                loadingContent = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .testTag(stringResource(R.string.testTag_favoriteList_onLoading))
                    )
                },
                failureContent = { exception ->
                    Text(
                        modifier = Modifier
                            .testTag(stringResource(R.string.testTag_favoriteList_onFailure)),
                        text = exception.message ?: FetchPlaceException().message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteDetailPaneComponentOnSuccessPreview() {
    val response = Response.Success(mockFavoritePlaces)
    FavoriteListPaneComponent(
        state = FavoriteState(fetchEntries = response),
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
        state = FavoriteState(fetchEntries = response),
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onAddNote = { _, _ -> },
        onFavoriteIconClick = {},
        onDeleteNote = {},
        onNavigateToDetails = {},
    )
}
