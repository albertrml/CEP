package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.arml.cep.ui.screen.component.place.PlaceFilterComponent
import br.com.arml.cep.ui.utils.favoriteFilterOptions

@Composable
fun FavoritePlaceFilter(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit = {},
    onFilterByTitle: (String) -> Unit = {},
    onNoneFilter: () -> Unit = {}
) {
    PlaceFilterComponent(
        modifier = modifier,
        filters = favoriteFilterOptions,
        onFilterByCep = { onFilterByCep(it) },
        onFilterByTitle = { onFilterByTitle(it) },
        onNoneFilter = { onNoneFilter() }
    )
}