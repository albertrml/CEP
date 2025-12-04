package br.com.arml.cep.ui.screen.component.common.filter.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions

@Composable
fun PlaceFilterList(
    modifier: Modifier = Modifier,
    filters: List<PlaceFilterOption>,
    selectedFilter: PlaceFilterOption,
    onSelectedFilter: (PlaceFilterOption) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(filters) { topic ->
            PlaceFilterChip(
                labelFilter = topic,
                isSelected = topic === selectedFilter,
                //selectedLabelFilter = selectedFilter,
                onSelected = { topic -> onSelectedFilter(topic) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceFilterListPreview(){
    PlaceFilterList(
        filters = favoriteFilterOptions,
        selectedFilter = PlaceFilterOption.None,
        onSelectedFilter = {}
    )
}