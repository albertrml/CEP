package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun CachePlaceAlert(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onChangeVisibility: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit,
) {
    if (isVisible)
    {
        CepAlertDialog(
            modifier = modifier,
            dialogTitle = stringResource(R.string.cache_title_alert),
            dialogText = stringResource(R.string.cache_text_alert),
            onDismissRequest = {
                onDismissRequest()
                onChangeVisibility(false)
            },
            onConfirmationRequest = {
                onConfirmationRequest()
                onChangeVisibility(false)
            }
        )
    }
}