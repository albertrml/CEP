package br.com.arml.cep.ui.screen.component.log.listpane.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.LogFilterOption

@Composable
fun LogFilterList(
    modifier: Modifier = Modifier,
    filters: List<LogFilterOption>,
    selectedFilter: LogFilterOption,
    onSelectedFilter: (LogFilterOption) -> Unit
) {
    LazyRow(
        modifier = modifier
            .testTag(stringResource(R.string.logFilterList_component_testTag)),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(filters) { topic ->
            LogFilterChip(
                labelFilter = topic,
                isSelected = topic === selectedFilter,
                onSelected = { topic -> onSelectedFilter(topic) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogFilterListPreview(){
    val filters = listOf(
        LogFilterOption.None,
        LogFilterOption.ByCep,
        LogFilterOption.ByInitialDate,
        LogFilterOption.ByFinalDate,
        LogFilterOption.ByRangeDate
    )
    LogFilterList(
        filters = filters,
        selectedFilter = filters.first(),
        onSelectedFilter = {}
    )
}
