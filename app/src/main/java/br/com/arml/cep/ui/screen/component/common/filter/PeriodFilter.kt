package br.com.arml.cep.ui.screen.component.common.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.toEndOfDay
import br.com.arml.cep.model.utils.toStartOfDay
import br.com.arml.cep.ui.screen.component.common.datepicker.DatePickerField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun PeriodFilter(
    modifier: Modifier = Modifier,
    onFilterByRange: (Long, Long) -> Unit
) {
    var initialDate by remember { mutableStateOf<Long?>(null) }
    var finalDate by remember { mutableStateOf<Long?>(null) }
    val isError = run {
        val initial = initialDate
        val final = finalDate
        if (initial != null && final != null) {
            initial >= final
        } else false
    }
    val isButtonActive = initialDate != null && finalDate != null && !isError

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.periodFilter_composable_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        DatePickerField(
            modifier = Modifier
                .testTag(stringResource(R.string.periodFilter_startDateField_testTag)),
            label = stringResource(R.string.periodFilter_startDateField_label),
            date = initialDate,
            onSelectDate = { initialDate = it?.toStartOfDay() }
        )
        DatePickerField(
            modifier = Modifier
                .testTag(stringResource(R.string.periodFilter_endDateField_testTag)),
            label = stringResource(R.string.periodFilter_endDateField_label),
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(text = stringResource(R.string.periodFilter_endDateField_supportText))
                }
            },
            date = finalDate,
            onSelectDate = { finalDate = it?.toEndOfDay() }
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.periodFilter_filterButton_testTag)),
            enabled = isButtonActive,
            onClick = { onFilterByRange(initialDate!!, finalDate!!) },
        ) {
            Text(text = stringResource(R.string.periodFilter_filterButton_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PeriodFilterPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        PeriodFilter(
            onFilterByRange = { _, _ -> }
        )
    }
}