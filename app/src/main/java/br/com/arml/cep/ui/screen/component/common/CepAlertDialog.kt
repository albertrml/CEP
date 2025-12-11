package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepAlertDialog(
    modifier: Modifier = Modifier,
    dialogTitle: String,
    dialogText: String,
    isVisibility: Boolean = true,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit
) {
    if(isVisibility){
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
                TextButton(
                    border = BorderStroke(
                        width = MaterialTheme.dimens.smallThickness,
                        color = MaterialTheme.colorScheme.error
                    ),
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(R.string.cepAlertdialog_dismissButton_text),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                TextButton(
                    border = BorderStroke(
                        width = MaterialTheme.dimens.smallThickness,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    onClick = onConfirmationRequest
                ) {
                    Text(
                        text = stringResource(R.string.cepAlertdialog_confirmButton_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun CepAlertDialogPreview() {
    CepAlertDialog(
        modifier = Modifier,
        dialogTitle = stringResource(R.string.log_delete_all_log_title),
        dialogText = stringResource(R.string.log_delete_all_log_alert),
        onDismissRequest = {},
        onConfirmationRequest = {}
    )
}