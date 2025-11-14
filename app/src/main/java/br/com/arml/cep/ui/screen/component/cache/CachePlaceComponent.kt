package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.common.DeleteAllComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun CachePlaceListComponent(
    modifier: Modifier = Modifier,
    fetchResponse: Response<List<Place>>,
    onFavoriteIconClick: (Place) -> Unit,
    onDeleteIconClick: (Place) -> Unit,
    onCepFilter: (String) -> Unit,
    onClearFilter: () -> Unit,
    onNavigateToDetail: (Place) -> Unit,
    onDeleteCacheClick: () -> Unit,
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        CachePlaceHeaderList()
        CachePlaceFilter(
            modifier = Modifier.fillMaxWidth(),
            onFilterByCep = { query -> onCepFilter(query) },
            onNoneFilter = { onClearFilter() }
        )
        DeleteAllComponent(
            deleteLogAlertTitleId = R.string.cache_title_alert,
            deleteLogAlertTextId = R.string.cache_text_alert,
            onConfirmDeleteAllEntries = { onDeleteCacheClick() }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ){
            fetchResponse.ShowResults(
                successContent = { places ->
                    CachePlaceList(
                        modifier = Modifier.align(Alignment.TopCenter),
                        places = places,
                        onDeleteIconClick = { place -> onDeleteIconClick(place) },
                        onFavoriteIconClick = { place -> onFavoriteIconClick(place) },
                        onNavigateToDetail = { place -> onNavigateToDetail(place) }
                    )
                },
                loadingContent = {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                },
                failureContent = { exception ->
                    Text(
                        modifier = Modifier.align(Alignment.Center),
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
fun CachePlaceListComponentPreview(){
    CachePlaceListComponent(
        fetchResponse = Response.Success(mockUnfavoritePlaces),
        onFavoriteIconClick = {},
        onDeleteIconClick = {},
        onCepFilter = {},
        onClearFilter = {},
        onNavigateToDetail = {},
        onDeleteCacheClick = {}
    )
}