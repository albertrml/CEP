package br.com.arml.cep.ui.screen.component.log

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun DeleteAllLogAlert(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    @StringRes dialogTitleId: Int,
    @StringRes dialogTextId: Int,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit
){
    if (showDialog) {
        CepAlertDialog(
            modifier = modifier,
            /*dialogTitle = stringResource(R.string.log_delete_all_log_title),
            dialogText = stringResource(R.string.log_delete_all_log_alert),*/
            dialogTitle = stringResource(dialogTitleId),
            dialogText = stringResource(dialogTextId),
            onDismissRequest = onDismissRequest,
            onConfirmationRequest = onConfirmationRequest
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteAllLogAlertPreview(){
    DeleteAllLogAlert(
        showDialog = true,
        dialogTitleId = R.string.log_delete_all_log_title,
        dialogTextId = R.string.log_delete_all_log_alert,
        onDismissRequest = {},
        onConfirmationRequest = {}
    )
}