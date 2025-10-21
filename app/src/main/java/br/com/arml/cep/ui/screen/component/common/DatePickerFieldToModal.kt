@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import br.com.arml.cep.model.entity.addCurrentHour
import br.com.arml.cep.model.entity.toFormattedBR

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

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        modifier = Modifier.testTag(stringResource(R.string.testTag_datePickerModal)),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .testTag(stringResource(R.string.testTag_datePicker_confirmTextButton)),
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis?.addCurrentHour())
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.date_picker_confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier
                    .testTag(stringResource(R.string.testTag_datePicker_cancelTextButton)),
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.date_picker_confirm_cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
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