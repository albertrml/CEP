package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.ui.screen.component.common.ScrollableFab
import br.com.arml.cep.ui.screen.component.place.PlaceElement
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoritePlaceList(
    modifier: Modifier = Modifier,
    places: List<PlaceEntry>,
    onFavoriteIconClick: (PlaceEntry) -> Unit,
    onNavigateToDetail: (PlaceEntry) -> Unit
) {
    val lazyListState = rememberLazyListState()
    ScrollableFab(listState = lazyListState) {
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            items(places) { place ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        modifier = Modifier.clickable(onClick = { onNavigateToDetail(place) }),
                        shape = MaterialTheme.shapes.small
                    ) {
                        PlaceElement(
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.dimens.smallPadding)
                                .padding(start = MaterialTheme.dimens.mediumPadding),
                            placeEntry = place,
                            onFavoriteIconClick = onFavoriteIconClick,
                            favoriteIcon = Icons.Default.Favorite,
                            colorFavoriteIcon = Color.Red
                        )
                    }
                }
            }
        }
    }
}