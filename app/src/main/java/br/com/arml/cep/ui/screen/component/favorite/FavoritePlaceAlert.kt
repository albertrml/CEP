package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun FavoritePlaceAlert(
    modifier: Modifier = Modifier,
    place: PlaceEntry?,
    onConfirmationRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    place?.apply {
        val title = note?.title
            ?: stringResource(R.string.favorite_unwanted_alert_title)
        val text = stringResource(
            R.string.favorite_unwanted_alert_text,
            address.zipCode
        )
        CepAlertDialog(
            modifier = modifier,
            dialogTitle = title,
            dialogText = text,
            onDismissRequest = onDismissRequest,
            onConfirmationRequest = onConfirmationRequest
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePlaceAlertPreview() {
    FavoritePlaceAlert(
        place = mockPlaceEntries.first(),
        onConfirmationRequest = {},
        onDismissRequest = {}
    )
}