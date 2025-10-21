package br.com.arml.cep.ui.screen.component.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import br.com.arml.cep.model.domain.CEP_LENGTH
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_CEP_LENGTH_FOR_SEARCH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.ui.screen.component.search.SearchCepField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepFilter(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit
) {
    var zipCode by remember { mutableStateOf("") }
    val isButtonActive by remember {
        derivedStateOf {
            zipCode.length >= MIN_CEP_LENGTH_FOR_SEARCH && zipCode.length <= CEP_LENGTH
        }
    }

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.testTag_cepFilter_composable)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        SearchCepField(
            modifier = modifier
                .testTag(stringResource(R.string.testTag_cepFilter_searchField)),
            onQueryChange = { zipCode = it }
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_cepFilter_searchButton)),
            enabled = isButtonActive,
            onClick = { onFilterByCep(zipCode) },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Composable
fun TitleFilter(
    modifier: Modifier = Modifier,
    nameFilter: String,
    maxSize: Int,
    onFilterByTitle: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val isButtonActive by remember {
        derivedStateOf {
            text.length >= MIN_TITLE_LENGTH && text.length <= MAX_TITLE_LENGTH
        }
    }

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.testTag_titleFilter_composable)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CepTextField(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_titleFilter_field)),
            nameField = nameFilter,
            text = text,
            onChangeText = { newText -> text = newText },
            maxSize = maxSize
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_titleFilter_button)),
            enabled = isButtonActive,
            onClick = { onFilterByTitle(text) },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Composable
fun SingleDateFilter(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    onFilterByDate: (Long) -> Unit
) {
    var initialDate by remember { mutableStateOf<Long?>(null) }
    val isButtonActive = initialDate != null

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.testTag_singleDateFilter_composable)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        DatePickerFieldToModal(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_singleDateFilter_field)),
            label = stringResource(labelId),
            onSelectDate = { initialDate = it }
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_singleDateFilter_button)),
            enabled = isButtonActive,
            onClick = { initialDate?.let { onFilterByDate(it) } },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Composable
fun PeriodFilter(
    modifier: Modifier = Modifier,
    onFilterByInitialDate: (Long, Long) -> Unit
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
            .testTag(stringResource(R.string.testTag_periodFilter_composable)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        DatePickerFieldToModal(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_periodFilter_startDateField)),
            label = stringResource(R.string.log_filter_initial_date_label),
            onSelectDate = { initialDate = it }
        )
        DatePickerFieldToModal(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_periodFilter_endDateField)),
            label = stringResource(R.string.log_filter_final_date_label),
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(text = stringResource(R.string.log_filter_final_date_support_text))
                }
            },
            onSelectDate = { finalDate = it }
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_periodFilter_button)),
            enabled = isButtonActive,
            onClick = { onFilterByInitialDate(initialDate!!, finalDate!!) },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CepFilterPreview() {
    CepFilter(
        onFilterByCep = {}
    )
}

@Preview(showBackground = true)
@Composable
fun FieldFilterPreview() {
    TitleFilter(
        nameFilter = "Name",
        maxSize = 100,
        onFilterByTitle = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SingleDateFilterPreview() {
    SingleDateFilter(
        labelId = R.string.log_filter_initial_date_label,
        onFilterByDate = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PeriodFilterPreview() {
    PeriodFilter(
        onFilterByInitialDate = { _, _ -> }
    )
}