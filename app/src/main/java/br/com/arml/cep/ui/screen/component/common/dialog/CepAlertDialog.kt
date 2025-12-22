package br.com.arml.cep.ui.screen.component.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R

@Composable
fun CepAlertDialog(
    modifier: Modifier = Modifier,
    dialogTitle: String,
    dialogText: String,
    isVisibility: Boolean = true,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit
) {
    if (isVisibility) {
        AlertDialog(
            modifier = modifier
                .testTag(stringResource(R.string.cepAlertdialog_component_testTag)),
            title = {
                Text(
                    text = dialogTitle,
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                Text(
                    text = dialogText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onDismissRequest = onDismissRequest,
            dismissButton = {
                CepAlertDialogDismissButton(onDismissRequest = onDismissRequest)
            },
            confirmButton = {
                CepAlertDialogConfirmationButton(onConfirmationRequest = onConfirmationRequest)
            }
        )
    }
}

@Preview
@Composable
fun CepAlertDialogPreview() {
    CepAlertDialog(
        modifier = Modifier,
        dialogTitle = stringResource(R.string.cepAlertDialog_mockTitle_text),
        dialogText = stringResource(R.string.cepAlertDialog_mockContent_text),
        onDismissRequest = {},
        onConfirmationRequest = {}
    )
}