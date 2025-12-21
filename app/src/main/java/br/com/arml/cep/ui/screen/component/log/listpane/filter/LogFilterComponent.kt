package br.com.arml.cep.ui.screen.component.log.listpane.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.filter.CepFilter
import br.com.arml.cep.ui.screen.component.common.filter.DateFilter
import br.com.arml.cep.ui.screen.component.common.filter.PeriodFilter
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.LogFilterOption
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.filterEnterTransition
import br.com.arml.cep.ui.utils.filterExitTransition
import br.com.arml.cep.ui.utils.logFilterOptions

const val oneSecondForTomorrow = 86399000L

@Composable
fun LogFilterComponent(
    modifier: Modifier = Modifier,
    selectedFilter: LogFilterOption,
    onFilterChange: (LogFilterOption) -> Unit,
    onFilterByCep: (String) -> Unit,
    onFilterByInitialDate: (Long) -> Unit,
    onFilterByFinalDate: (Long) -> Unit,
    onFilterByRangeDate: (Long, Long) -> Unit,
    onNoneFilter: () -> Unit
) {
    val filters = logFilterOptions
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogFilterList(
            modifier = Modifier
                .testTag(stringResource(R.string.logFilterComponent_component_testTag)),
            filters = filters,
            selectedFilter = selectedFilter,
            onSelectedFilter = {
                onFilterChange(it)
                if (it == PlaceFilterOption.None) {
                    onNoneFilter()
                }
            }
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
                    CepFilter(
                        onFilterByCep = { cep ->
                            keyboardController?.hide()
                            onFilterByCep(cep)
                        }
                    )
                }

                LogFilterOption.ByInitialDate -> {
                    DateFilter(
                        modifier = Modifier
                            .testTag(stringResource(R.string.logFilterComponent_initialDate_testTag)),
                        labelId = R.string.logFilterComponent_initialDate_label,
                        onFilterByDate = { start ->
                            keyboardController?.hide()
                            onFilterByInitialDate(start)
                        }
                    )
                }

                LogFilterOption.ByFinalDate -> {
                    DateFilter(
                        modifier = Modifier
                            .testTag(stringResource(R.string.logFilterComponent_finalDate_testTag)),
                        labelId = R.string.logFilterComponent_finalDate_label,
                        onFilterByDate = { end ->
                            keyboardController?.hide()
                            onFilterByFinalDate(end + oneSecondForTomorrow)
                        }
                    )
                }

                LogFilterOption.ByRangeDate -> {
                    PeriodFilter(
                        onFilterByInitialDate = { start, end ->
                            keyboardController?.hide()
                            onFilterByRangeDate(start, end + oneSecondForTomorrow)
                        }
                    )
                }
                LogFilterOption.None -> { keyboardController?.hide() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogFilterPreview() {
    var selectedFilter by rememberSaveable(stateSaver = LogFilterOption.saver) {
        mutableStateOf(LogFilterOption.None)
    }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LogFilterComponent(
            modifier = Modifier
                .padding(MaterialTheme.dimens.largeMargin),
            selectedFilter = selectedFilter,
            onFilterChange = { selectedFilter = it },
            onFilterByCep = {},
            onFilterByInitialDate = {},
            onFilterByFinalDate = {},
            onFilterByRangeDate = { _, _ -> },
            onNoneFilter = {}
        )
    }
}