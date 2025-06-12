package br.com.arml.cep.ui.screen.component.log

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.PlaceEntry
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
            onConfirmation = onConfirmation
        )
    }
}

@Composable
fun UnwantedEntryAlert(
    modifier: Modifier = Modifier,
    entry: PlaceEntry,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
){
    if (showDialog) {
        CepAlertDialog(
            modifier = modifier,
            dialogTitle = entry.note?.title?: stringResource(R.string.favorite_unwanted_alert_title),
            dialogText = stringResource(R.string.favorite_unwanted_alert_text, entry.address.zipCode),
            onDismissRequest = onDismissRequest,
            onConfirmation = onConfirmation
        )
    }
}