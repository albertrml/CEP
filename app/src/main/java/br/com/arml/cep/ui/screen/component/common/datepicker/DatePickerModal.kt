package br.com.arml.cep.ui.screen.component.common.datepicker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.addCurrentHour

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
fun DatePickerModalPreview() {
    DatePickerModal(
        onDateSelected = {},
        onDismiss = {}
    )
}