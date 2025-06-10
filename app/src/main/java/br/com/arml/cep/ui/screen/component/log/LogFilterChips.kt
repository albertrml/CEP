package br.com.arml.cep.ui.screen.component.log

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.LogFilterOption
import br.com.arml.cep.ui.utils.filterEnterTransition
import br.com.arml.cep.ui.utils.filterExitTransition
import br.com.arml.cep.ui.utils.logFilterOptions

const val oneSecondForTomorrow = 86399000L

@Composable
fun LogFilterComponent(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit,
    onFilterByInitialDate: (Long) -> Unit,
    onFilterByFinalDate: (Long) -> Unit,
    onFilterByRangeDate: (Long, Long) -> Unit,
    onNoneFilter: () -> Unit
) {
    val filters = logFilterOptions
    var selectedFilter by rememberSaveable(stateSaver = LogFilterOption.saver) {
        mutableStateOf<LogFilterOption>(LogFilterOption.None)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogFilterChips(
            filters = filters,
            selectedFilter = selectedFilter,
            onSelectedFilter = { selectedFilter = it }
        )
        AnimatedContent(
            targetState = selectedFilter,
            transitionSpec = {
                filterEnterTransition
                    .togetherWith(filterExitTransition) using SizeTransform(clip = true)
            }
        ) { targetFilter ->
            when (targetFilter) {
                LogFilterOption.ByCep -> {
                    CepFilter(onFilterByCep = { onFilterByCep(it) })
                }

                LogFilterOption.ByInitialDate -> {
                    SingleDateFilter(onFilterByInitialDate = { onFilterByInitialDate(it) })
                }

                LogFilterOption.ByFinalDate -> {
                    SingleDateFilter(
                        onFilterByInitialDate = {
                            onFilterByFinalDate(it + oneSecondForTomorrow)
                        }
                    )
                }

                LogFilterOption.ByRangeDate -> {
                    PeriodFilter(
                        onFilterByInitialDate = { start, end ->
                            onFilterByRangeDate(start, end + oneSecondForTomorrow)
                        }
                    )
                }
                
                else -> {
                    onNoneFilter()
                }
            }
        }
    }
}

@Composable
fun LogFilterChips(
    modifier: Modifier = Modifier,
    filters: List<LogFilterOption>,
    selectedFilter: LogFilterOption,
    onSelectedFilter: (LogFilterOption) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { topic ->
            LogFilterChip(
                labelFilter = topic,
                isSelected = topic === selectedFilter,
                onSelected = { onSelectedFilter(it) }
            )
        }
    }
}

@Composable
fun LogFilterChip(
    modifier: Modifier = Modifier,
    labelFilter: LogFilterOption,
    isSelected: Boolean,
    onSelected: (LogFilterOption) -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = { onSelected(labelFilter) },
        label = { Text(labelFilter.name) },
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(
                        R.string.log_selected_filter_description,
                        labelFilter.name
                    ),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        end = MaterialTheme.dimens.smallSpacing
                    )
                )
            } else {
                null
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LogFilterPreview2() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LogFilterComponent(
            modifier = Modifier
                .padding(top = MaterialTheme.dimens.largeMargin * 2)
                .padding(horizontal = MaterialTheme.dimens.mediumMargin),
            onFilterByCep = {},
            onFilterByInitialDate = {},
            onFilterByFinalDate = {},
            onFilterByRangeDate = { _, _ -> },
            onNoneFilter = {}
        )
    }
}