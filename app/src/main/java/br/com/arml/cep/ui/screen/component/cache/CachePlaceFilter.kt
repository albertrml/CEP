package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.ui.screen.component.common.filter.chip.PlaceFilterComponent
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

@Preview(showBackground = true)
@Composable
fun CachePlaceFilterPreview() {
    CachePlaceFilter()
}