package br.com.arml.cep.ui.screen.component.favorite.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun FavoriteListComponent(
    modifier: Modifier = Modifier,
    state: FavoriteState,
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
    val addNoteResponse = state.addNoteEntry
    val deleteNoteResponse = state.deleteNoteEntry

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(fetchResponse) {
        when (fetchResponse) {
            is Response.Failure -> {
                val exception = fetchResponse.exception
                val message = exception.message ?: exception.javaClass.simpleName
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    LaunchedEffect(addNoteResponse) {
        when(addNoteResponse){
            is Response.Success -> {
                val zipcode = addNoteResponse.result
                snackbarHostState.showSnackbar(
                    message = "Nota adicionada ao cep $zipcode",
                    duration = SnackbarDuration.Short
                )
            }
            is Response.Failure -> {
                val exception = addNoteResponse.exception
                val message = exception.message ?: exception.javaClass.simpleName
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
            Response.Loading -> {}
        }
    }

    LaunchedEffect(deleteNoteResponse) {
        when(deleteNoteResponse){
            is Response.Success -> {
                val zipcode = deleteNoteResponse.result
                snackbarHostState.showSnackbar(
                    message = "Nota deletada do cep $zipcode",
                    duration = SnackbarDuration.Short
                )
            }
            is Response.Failure -> {
                val exception = deleteNoteResponse.exception
                val message = exception.message ?: exception.javaClass.simpleName
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
        ) {
            FavoriteListHeader(
                modifier = Modifier
                    .testTag(stringResource(R.string.testTag_favoriteList_header)),
                onImportClick = onImportClick,
                onExportClick = onExportClick
            )

            FavoriteFilter(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(R.string.testTag_favoriteList_filter)),
                onFilterByCep = { query -> onCepFilter(query) },
                onFilterByTitle = { query -> onTitleFilter(query) },
                onNoneFilter = { onNoneFilter() }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                fetchResponse.ShowResults(
                    successContent = { places ->
                        FavoriteListOnSuccess(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .testTag(stringResource(R.string.testTag_favoriteList_onSuccess)),
                            places = places,
                            onAddNote = onAddNote,
                            onFavoriteIconClick = onFavoriteIconClick,
                            onDeleteNote = onDeleteNote,
                            onNavigateToDetails = onNavigateToDetails
                        )
                    },
                    loadingContent = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .testTag(stringResource(R.string.testTag_favoriteList_onLoading))
                        )
                    },
                    failureContent = { exception ->
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
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
}

@Composable
fun FavoriteListOnSuccess(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onAddNote: (Cep, Note) -> Unit,
    onFavoriteIconClick: (Place) -> Unit,
    onDeleteNote: (Pair<Cep, Note>) -> Unit,
    onNavigateToDetails: (Pair<Address, Note>) -> Unit,
) {
    Column(modifier = modifier) {
        FavoriteList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.dimens.mediumSpacing)
                .testTag(stringResource(R.string.testTag_favoriteList_list)),
            places = places,
            onAddNote = onAddNote,
            onFavoriteIconClick = onFavoriteIconClick,
            onNavigateToDetail = onNavigateToDetails,
            onDeleteNote = onDeleteNote,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteListComponentOnSuccessPreview() {
    FavoriteListComponent(
        state = FavoriteState(fetchEntries = Response.Success(mockFavoritePlaces)),
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
fun FavoriteListComponentOnLoadingPreview() {
    FavoriteListComponent(
        state = FavoriteState(fetchEntries = Response.Success(mockFavoritePlaces)),
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
fun FavoriteListComponentOnFailurePreview() {
    FavoriteListComponent(
        state = FavoriteState(fetchEntries = Response.Success(mockFavoritePlaces)),
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onAddNote = { _, _ -> },
        onFavoriteIconClick = {},
        onDeleteNote = {},
        onNavigateToDetails = {},
    )
}
