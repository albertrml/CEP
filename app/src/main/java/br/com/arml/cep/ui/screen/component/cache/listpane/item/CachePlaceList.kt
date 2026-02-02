package br.com.arml.cep.ui.screen.component.cache.listpane.item

import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollGrid

@Composable
fun CachePlaceList(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onDeletePlace: (Place) -> Unit,
    onNavigateToDetail: (Place) -> Unit
) {
    FastScrollGrid (modifier = modifier) {
        items(places) { place ->
            CachePlaceElement(
                place = place,
                onDeletePlace = onDeletePlace,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CachePlaceListPreview() {
    CachePlaceList(
        places = mockUnfavoritePlaces,
        onDeletePlace = {},
        onNavigateToDetail = {},
    )
}