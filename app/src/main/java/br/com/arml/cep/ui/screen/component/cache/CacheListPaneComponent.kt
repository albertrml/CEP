package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.core.response.Response
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.cache.listpane.CacheListPaneOnFailure
import br.com.arml.cep.ui.screen.component.cache.listpane.CacheListPaneOnSuccess
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.core.response.ui.ShowResults

@Composable
fun CacheListPaneComponent(
    modifier: Modifier = Modifier,
    fetchResponse: Response<List<Place>>,
    onCepFilter: (String) -> Unit,
    onClearFilter: () -> Unit,
    onDeletePlace: (Place) -> Unit,
    onDeleteAllCache: () -> Unit,
    onNavigateToDetail: (Place) -> Unit,
) {
    var selectedFilter by rememberSaveable(stateSaver = PlaceFilterOption.saver) {
        mutableStateOf(PlaceFilterOption.None)
    }
    Scaffold(
        modifier = modifier
            .testTag(stringResource(R.string.cacheListPaneComponent_component_testTag)),
        topBar = {
            Header(
                modifier = Modifier.testTag(
                    stringResource(R.string.cacheListPaneComponent_header_testTag)
                ),
                logo = Icons.Default.Storage,
                title = stringResource(R.string.cacheListPaneComponent_header_title)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            fetchResponse.ShowResults(
                successContent = { places ->
                    CacheListPaneOnSuccess(
                        places = places,
                        selectedFilter = selectedFilter,
                        onSelectedFilter = { selectedFilter = it },
                        onCepFilter = { query -> onCepFilter(query) },
                        onClearFilter = { onClearFilter() },
                        onDeletePlace = { place -> onDeletePlace(place) },
                        onDeleteAllCache = { onDeleteAllCache() },
                        onNavigateToDetail = { place -> onNavigateToDetail(place) }
                    )
                },
                /*loadingContent = {
                    CacheListPaneOnLoading(modifier = Modifier.align(Alignment.Center))
                },*/
                failureContent = { exception ->
                    CacheListPaneOnFailure(
                        modifier = Modifier.align(Alignment.Center),
                        failureMessage = exception.message ?: FetchPlaceException().message,
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CacheListPaneComponentPreview() {
    CacheListPaneComponent(
        fetchResponse = Response.Success(mockUnfavoritePlaces),
        onDeletePlace = {},
        onCepFilter = {},
        onClearFilter = {},
        onNavigateToDetail = {},
        onDeleteAllCache = {}
    )
}