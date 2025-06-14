package br.com.arml.cep.ui.screen.component.log

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun DeleteAllLogAlert(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
){
    if (showDialog) {
        CepAlertDialog(
            modifier = modifier,
            dialogTitle = stringResource(R.string.log_delete_all_log_title),
            dialogText = stringResource(R.string.log_delete_all_log_alert),
            onDismissRequest = onDismissRequest,
            onConfirmationRequest = onConfirmation
        )
    }
}