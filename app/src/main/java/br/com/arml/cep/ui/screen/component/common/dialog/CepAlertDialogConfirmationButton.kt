package br.com.arml.cep.ui.screen.component.common.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepAlertDialogConfirmationButton(
    modifier: Modifier = Modifier,
    onConfirmationRequest: () -> Unit
) {
    TextButton(
        modifier = modifier,
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