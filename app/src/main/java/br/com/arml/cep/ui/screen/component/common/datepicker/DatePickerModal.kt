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
import br.com.arml.cep.model.utils.adjustDay

@Composable
fun DatePickerModal(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        modifier = modifier
            .testTag(stringResource(R.string.datePickerModal_component_testTag)),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .testTag(stringResource(R.string.datePickerModal_confirmButton_testTag)),
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis?.adjustDay())
                }
            ) {
                Text(stringResource(R.string.datePickerModal_confirmButton_label))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier
                    .testTag(stringResource(R.string.datePickerModal_dismissButton_testTag)),
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.datePickerModal_dismissButton_label))
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