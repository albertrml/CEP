package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.ui.screen.component.common.CepAlertDialog

@Composable
fun FavoritePlaceAlert(
    modifier: Modifier = Modifier,
    place: PlaceEntry,
    isVisible: Boolean,
    onChangeVisibility: (Boolean) -> Unit,
    onConfirmationRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val title = place.note?.title
        ?: stringResource(R.string.favorite_unwanted_alert_title)
    val text = stringResource(
        R.string.favorite_unwanted_alert_text,
        place.address.zipCode
    )

    if (isVisible) {
        CepAlertDialog(
            modifier = modifier,
            dialogTitle = title,
            dialogText = text,
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