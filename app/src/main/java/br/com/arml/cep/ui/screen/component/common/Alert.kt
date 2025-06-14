package br.com.arml.cep.ui.screen.component.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R

@Composable
fun CepAlertDialog(
    modifier: Modifier = Modifier,
    dialogTitle: String,
    dialogText: String,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmationRequest) {
                Text(
                    text = stringResource(R.string.alert_dialog_confirm_button),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier,
                onClick = onDismissRequest) {
                Text(
                    text = stringResource(R.string.alert_dialog_dismiss_button),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    )

}

@Preview
@Composable
fun InsightAlertDialogPreview() {
    CepAlertDialog(
        modifier = Modifier,
        dialogTitle = stringResource(R.string.log_delete_all_log_title),
        dialogText = stringResource(R.string.log_delete_all_log_alert),
        onDismissRequest = {},
        onConfirmationRequest = {}
    )
}