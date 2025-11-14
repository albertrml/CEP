package br.com.arml.cep.ui.screen.component.favorite.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.common.ScrollableFab
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteList(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onDeleteNote: (Pair<Cep,Note>) -> Unit,
    onFavoriteIconClick: (Place) -> Unit,
    onNavigateToDetail: (Pair<Address, Note>) -> Unit
) {
    val lazyListState = rememberLazyListState()
    ScrollableFab(listState = lazyListState) {
        LazyColumn(
            modifier = modifier.padding(vertical = MaterialTheme.dimens.smallPadding),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            items(places) { place ->
                FavoriteElement(
                    place = place,
                    favoriteIcon = Icons.Default.Favorite,
                    colorFavoriteIcon = Color.Red,
                    onNavigateToDetail = onNavigateToDetail,
                    onFavoriteIconClick = onFavoriteIconClick,
                    onDeleteNote = onDeleteNote
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteListPreview(){
    FavoriteList(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        places = mockFavoritePlaces,
        onFavoriteIconClick = {},
        onNavigateToDetail = {},
        onDeleteNote = {}
    )
}