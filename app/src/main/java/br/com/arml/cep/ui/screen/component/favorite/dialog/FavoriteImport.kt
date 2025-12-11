package br.com.arml.cep.ui.screen.component.favorite.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun FavoriteImport(
    modifier: Modifier = Modifier,
    isVisibility: Boolean,
    onConfirmationRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    CepAlertDialog(
        modifier = modifier
            .testTag(stringResource(R.string.favoriteImport_component_testTag)),
        dialogTitle = stringResource(R.string.favoriteImport_title_text),
        dialogText = stringResource(R.string.favoriteImport_message_text),
        isVisibility = isVisibility,
        onDismissRequest = onDismissRequest,
        onConfirmationRequest = onConfirmationRequest,
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