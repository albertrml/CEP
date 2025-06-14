package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_CONTENT_LENGTH
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.model.domain.isValidTitleNoteSize
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockFavoritePlaceEntries
import br.com.arml.cep.ui.screen.component.cep.display.AddressScreen
import br.com.arml.cep.ui.screen.component.common.CepTextField
import br.com.arml.cep.ui.screen.component.place.favorite.FavoritePlaceHeaderDetails
import br.com.arml.cep.ui.screen.component.place.favorite.FavoritePlaceUpdateButton
import br.com.arml.cep.ui.theme.dimens

@Composable
fun PlaceDetailsComponent(
    modifier: Modifier,
    placeEntry: PlaceEntry,
    onClickToUpdate: (PlaceEntry) -> Unit,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf(placeEntry.note?.title ?: "") }
    var note by remember { mutableStateOf(placeEntry.note?.content ?: "") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FavoritePlaceHeaderDetails(onNavigateBackToList = onNavigateBack)
        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing))
        CepTextField(
            modifier = Modifier.fillMaxWidth(),
            nameField = stringResource(R.string.place_details_title),
            text = title,
            textStyle = MaterialTheme.typography.titleMedium,
            onChangeText = { title = it },
            maxSize = MAX_TITLE_LENGTH,
            isError = !title.isValidTitleNoteSize(),
            errorMessage = stringResource(
                R.string.favorite_details_title_error_msg,
                MIN_TITLE_LENGTH,
                MAX_TITLE_LENGTH
            )
        )
        CepTextField(
            modifier = Modifier.fillMaxWidth(),
            nameField = stringResource(R.string.place_details_content),
            text = note,
            onChangeText = { note = it },
            maxSize = MAX_CONTENT_LENGTH,
            maxLines = 7
        )
        AddressScreen(
            modifier = Modifier.weight(1f),
            address = placeEntry.address
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing))
        FavoritePlaceUpdateButton(
            place = placeEntry,
            title = title,
            note = note,
            onClickToUpdate = onClickToUpdate
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PlaceDetailsPreview() {
    PlaceDetailsComponent(
        modifier = Modifier,
        placeEntry = mockFavoritePlaceEntries.first(),
        onClickToUpdate = {},
        onNavigateBack = {}
    )
}