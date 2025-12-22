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
fun DatePickerField(
    modifier: Modifier = Modifier,
    label: String,
    date: Long? = null,
    onSelectDate: (Long?) -> Unit = {},
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
            .testTag(stringResource(R.string.datePickerField_component_testTag)),
        value = date?.toFormattedBR() ?: "",
        onValueChange = { },
        label = { Text(label) },
        placeholder = {
            Text(stringResource(R.string.datePickerField_brazilDateTimePattern_text))
        },
        trailingIcon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = stringResource(
                    R.string.datePickerField_trailingIcon_description
                )
            )
        },
        readOnly = true,
        isError = isError,
        supportingText = supportingText,
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                onSelectDate(it)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerFieldPreview() {
    DatePickerField(
        label = "Date",
        onSelectDate = {}
    )
}