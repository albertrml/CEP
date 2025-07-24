package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun FavoriteBackup(
    modifier: Modifier = Modifier,
    dialogTitle: String,
    dialogText: String,
    isVisibility: Boolean,
    onConfirmationRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    if (isVisibility) {
        CepAlertDialog(
            modifier = modifier,
            dialogTitle = dialogTitle,
            dialogText = dialogText,
            onDismissRequest = onDismissRequest,
            onConfirmationRequest = onConfirmationRequest
        )
    }
}

@Composable
fun FavoriteExport(
    modifier: Modifier = Modifier,
    isVisibility: Boolean,
    onConfirmationRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    FavoriteBackup(
        modifier = modifier,
        dialogTitle = stringResource(R.string.favorite_list_export_title),
        dialogText = stringResource(R.string.favorite_list_export_msg),
        isVisibility = isVisibility,
        onDismissRequest = onDismissRequest,
        onConfirmationRequest = onConfirmationRequest,
    )
}

@Composable
fun FavoriteImport(
    modifier: Modifier = Modifier,
    isVisibility: Boolean,
    onConfirmationRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    FavoriteBackup(
        modifier = modifier,
        dialogTitle = stringResource(R.string.favorite_list_import_title),
        dialogText = stringResource(R.string.favorite_list_import_msg),
        isVisibility = isVisibility,
        onDismissRequest = onDismissRequest,
        onConfirmationRequest = onConfirmationRequest,
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteExportPreview() {
    FavoriteExport(isVisibility = true)
}

@Preview(showBackground = true)
@Composable
fun FavoriteImportPreview() {
    FavoriteImport(isVisibility = true)
}