@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.arml.cep.ui.screen.component.common.datepicker

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.toFormattedBR

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    label: String,
    onSelectDate: (Long) -> Unit,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.toFormattedBR() ?: "",
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text(stringResource(R.string.date_time_pattern_from_brazil)) },
        trailingIcon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = stringResource(R.string.date_picker_description)
            )
        },
        readOnly = true,
        isError = isError,
        supportingText = supportingText,
        modifier = modifier
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) { showModal = true }
                }
            }
            .testTag(stringResource(R.string.testTag_datePickerField))
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it
                it?.let { onSelectDate(it) }
            },
            onDismiss = { showModal = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerFieldToModalPreview() {
    DatePickerFieldToModal(
        label = "Date",
        onSelectDate = {}
    )
}