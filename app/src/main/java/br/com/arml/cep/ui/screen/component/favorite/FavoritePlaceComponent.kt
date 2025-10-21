package br.com.arml.cep.ui.screen.component.favorite

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.model.mock.mockFavoritePlaceEntries
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun FavoriteListComponent(
    modifier: Modifier = Modifier,
    fetchResponse: Response<List<PlaceEntry>>,
    onImportClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onFavoriteIconClick: (PlaceEntry) -> Unit,
    onCepFilter: (String) -> Unit,
    onTitleFilter: (String) -> Unit,
    onNoneFilter: () -> Unit,
    onNavigateToDetails: (PlaceEntry) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val isVisible by remember {
        derivedStateOf {
            fetchResponse is Response.Success
        }
    }


    LaunchedEffect(fetchResponse) {
        if (fetchResponse is Response.Failure) {
            val exception = fetchResponse.exception
            val message = exception.message ?: exception.javaClass.simpleName
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
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

            if (isVisible) {
                FavoriteFilter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(R.string.testTag_favoriteList_filter)),
                    onFilterByCep = { query -> onCepFilter(query) },
                    onFilterByTitle = { query -> onTitleFilter(query) },
                    onNoneFilter = { onNoneFilter() }
                )
            }

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
                            onFavoriteIconClick = onFavoriteIconClick,
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
    places: List<PlaceEntry>,
    onFavoriteIconClick: (PlaceEntry) -> Unit,
    onNavigateToDetails: (PlaceEntry) -> Unit
) {
    Column(modifier = modifier) {
        FavoriteList(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.testTag_favoriteList_list)),
            places = places,
            onFavoriteIconClick = { place -> onFavoriteIconClick(place) },
            onNavigateToDetail = { place -> onNavigateToDetails(place) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteListComponentOnSuccessPreview() {
    FavoriteListComponent(
        fetchResponse = Response.Success(mockFavoritePlaceEntries),
        onFavoriteIconClick = {},
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onNavigateToDetails = {}
    )
}


@Preview(showBackground = true)
@Composable
fun FavoriteListComponentOnLoadingPreview() {
    FavoriteListComponent(
        fetchResponse = Response.Loading,
        onFavoriteIconClick = {},
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onNavigateToDetails = {}
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteListComponentOnFailurePreview() {
    FavoriteListComponent(
        fetchResponse = Response.Failure(Exception("Error")),
        onFavoriteIconClick = {},
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onNavigateToDetails = {}
    )
}