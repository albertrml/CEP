package br.com.arml.cep.ui.screen.component.favorite.listpane

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun FavoriteExport(
    modifier: Modifier = Modifier,
    isVisibility: Boolean,
    onConfirmationRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    CepAlertDialog(
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
    CepAlertDialog(
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
    var isVisibility by rememberSaveable { mutableStateOf(true) }
    FavoriteExport(
        isVisibility = isVisibility,
        onDismissRequest = { isVisibility = false },
        onConfirmationRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteImportPreview() {
    var isVisibility by rememberSaveable { mutableStateOf(true) }
    FavoriteImport(
        isVisibility = isVisibility,
        onDismissRequest = { isVisibility = false },
        onConfirmationRequest = {}
    )
}