package br.com.arml.cep.ui.screen.component.common.filter

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import br.com.arml.cep.ui.screen.component.common.datepicker.DatePickerFieldToModal
import br.com.arml.cep.ui.theme.dimens

@Composable
fun DateFilter(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    onFilterByDate: (Long) -> Unit
) {
    var initialDate by remember { mutableStateOf<Long?>(null) }
    val isButtonActive = initialDate != null

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.singleDateFilter_component_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        DatePickerFieldToModal(
            modifier = Modifier
                .testTag(stringResource(R.string.singleDateFilter_startDateField_testTag)),
            label = stringResource(labelId),
            onSelectDate = { initialDate = it }
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.singleDateFilter_filterButton_testTag)),
            enabled = isButtonActive,
            onClick = { initialDate?.let { onFilterByDate(it) } },
        ) {
            Text(text = stringResource(R.string.singleDateFilter_filterButton_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateFilterPreview() {
    DateFilter(
        labelId = R.string.log_filter_initial_date_label,
        onFilterByDate = {}
    )
}