package br.com.arml.cep.ui.screen.component.place.cache

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.arml.cep.ui.screen.component.place.PlaceFilterComponent
import br.com.arml.cep.ui.utils.cacheFilterOptions

@Composable
fun CachePlaceFilter(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit = {},
    onNoneFilter: () -> Unit = {}
) {
    PlaceFilterComponent(
        modifier = modifier,
        filters = cacheFilterOptions,
        onFilterByCep = { onFilterByCep(it) },
        onNoneFilter = { onNoneFilter() }
    )
}