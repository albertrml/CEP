package br.com.arml.cep.ui.screen.component.favorite.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.common.dialog.CepAlertDialog

@Composable
fun FavoriteChangeAlert(
    modifier: Modifier = Modifier,
    place: Place?,
    onConfirmationRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    place?.apply {
        val title = place.cep.text
        val text = stringResource(
            R.string.favoriteChangeAlert_message_text,
            title
        )
        CepAlertDialog(
            modifier = modifier
                .testTag(stringResource(R.string.favoriteChangeAlert_component_testTag)),
            dialogTitle = title,
            dialogText = text,
            onDismissRequest = onDismissRequest,
            onConfirmationRequest = onConfirmationRequest
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteAlertPreview() {
    FavoriteChangeAlert(
        place = mockFavoritePlaces.first(),
        onConfirmationRequest = {},
        onDismissRequest = {}
    )
}