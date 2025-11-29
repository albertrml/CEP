package br.com.arml.cep.ui.screen.component.favorite.listpane

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.ui.screen.component.place.PlaceFilterComponent
import br.com.arml.cep.ui.utils.favoriteFilterOptions

@Composable
fun FavoriteFilter(
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

@Preview(showBackground = true)
@Composable
fun FavoriteFilterPreview() {
    FavoriteFilter()
}