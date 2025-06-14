package br.com.arml.cep.ui.screen.component.favorite

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
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.UnknownException.FetchPlaceException
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun FavoritePlaceListComponent(
    modifier: Modifier = Modifier,
    fetchResponse: Response<List<PlaceEntry>>,
    onFavoriteIconClick: (PlaceEntry) -> Unit,
    onCepFilter: (String) -> Unit,
    onTitleFilter: (String) -> Unit,
    onClearFilter: () -> Unit,
    onNavigateToDetail: (PlaceEntry) -> Unit,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        FavoritePlaceHeaderList()
        FavoritePlaceFilter(
            modifier = Modifier.fillMaxWidth(),
            onFilterByCep = { query -> onCepFilter(query) },
            onFilterByTitle = { query -> onTitleFilter(query) },
            onNoneFilter = { onClearFilter() }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            fetchResponse.ShowResults(
                successContent = { places ->
                    FavoritePlaceList(
                        modifier = Modifier.align(Alignment.TopCenter),
                        places = places,
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