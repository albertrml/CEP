package br.com.arml.cep.ui.screen.component.common

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
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.cep.search.CepField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepFilter(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit
){
    var zipCode by remember { mutableStateOf("") }
    val isButtonActive = zipCode.length >= 3

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        CepField(
            modifier = modifier,
            onQueryChange = { zipCode = it }
        )
        Button(
            enabled = isButtonActive,
            onClick = { onFilterByCep(zipCode) },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Composable
fun FieldFilter(
    modifier: Modifier = Modifier,
    nameFilter: String,
    maxSize: Int,
    onFilterByCep: (String) -> Unit,
){
    var text by remember { mutableStateOf("") }
    val isButtonActive = text.length >= 3

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        CepTextField(
            modifier = modifier,
            nameField = nameFilter,
            text = text,
            onChangeText = { newText -> text = newText },
            maxSize = maxSize,
        )
        Button(
            enabled = isButtonActive,
            onClick = { onFilterByCep(text) },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Composable
fun SingleDateFilter(
    modifier: Modifier = Modifier,
    onFilterByInitialDate: (Long) -> Unit
){
    var initialDate by remember { mutableStateOf<Long?>(null) }
    val isButtonActive = initialDate != null

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        DatePickerFieldToModal(
            modifier = modifier,
            label = stringResource(R.string.log_filter_initial_date_label),
            onSelectDate = { initialDate = it }
        )
        Button(
            enabled = isButtonActive,
            onClick = { initialDate?.let{ onFilterByInitialDate(it) } },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}

@Composable
fun PeriodFilter(
    modifier: Modifier = Modifier,
    onFilterByInitialDate: (Long, Long) -> Unit
){
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        DatePickerFieldToModal(
            modifier = modifier,
            label = stringResource(R.string.log_filter_initial_date_label),
            onSelectDate = { initialDate = it }
        )
        DatePickerFieldToModal(
            modifier = modifier,
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
            enabled = isButtonActive,
            onClick = { onFilterByInitialDate(initialDate!!,finalDate!!) },
        ) {
            Text(text = stringResource(R.string.log_filter_button))
        }
    }
}