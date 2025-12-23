package br.com.arml.cep.ui.screen.component.favorite.listpane

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollGrid
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.FavoriteItem
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteListComponent(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onFavoriteIconClick: (Place) -> Unit,
    onAddNote: (Cep, Note) -> Unit,
    onDeleteNote: (Pair<Cep, Note>) -> Unit,
    onNavigateToDetail: (Pair<Address, Note>) -> Unit
) {
    FastScrollGrid(modifier = modifier) {
        items(places) { place ->
            FavoriteItem(
                place = place,
                favoriteIcon = Icons.Default.Favorite,
                colorFavoriteIcon = Color.Red,
                onAddNote = onAddNote,
                onFavoriteIconClick = onFavoriteIconClick,
                onDeleteNote = onDeleteNote,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteListComponentPreview() {
    FavoriteListComponent(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        places = mockFavoritePlaces,
        onAddNote = { _, _ -> },
        onFavoriteIconClick = {},
        onNavigateToDetail = {},
        onDeleteNote = {}
    )
}