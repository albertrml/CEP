package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoritePlaceDetailsComponent(
    modifier: Modifier,
    placeEntry: PlaceEntry,
    onNavigateBackToList: () -> Unit,
    onNavigateToExtra: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        FavoritePlaceHeaderDetails(onNavigateBackToList = onNavigateBackToList)
        AddressScreen(
            modifier = Modifier.weight(1f),
            address = placeEntry.address
        )
        Button(
            modifier = Modifier.padding(MaterialTheme.dimens.mediumPadding),
            onClick = onNavigateToExtra
        ){
            Text(text = stringResource(R.string.favorite_address_button))
        }
    }
}


@Composable
fun FavoritePlaceExtraComponent(
    modifier: Modifier = Modifier,
    placeEntry: PlaceEntry,
    onClickToUpdate: (PlaceEntry) -> Unit,
    onNavigateBackToDetails: () -> Unit,
){
    var title by remember { mutableStateOf(placeEntry.note?.title ?: "") }
    var note by remember { mutableStateOf(placeEntry.note?.content ?: "") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FavoritePlaceHeaderDetails(
            modifier = Modifier,
            onNavigateBackToList = onNavigateBackToDetails
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.dimens.mediumSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
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
                minLines = 7,
                maxLines = 10
            )
            FavoritePlaceUpdateButton(
                place = placeEntry,
                title = title,
                note = note,
                onClickToUpdate = onClickToUpdate
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceDetailsPreview() {
    FavoritePlaceDetailsComponent(
        modifier = Modifier,
        placeEntry = mockFavoritePlaceEntries.first(),
        onNavigateBackToList = {},
        onNavigateToExtra = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PlaceExtraPreview() {
    FavoritePlaceExtraComponent(
        modifier = Modifier.fillMaxSize(),
        placeEntry = mockFavoritePlaceEntries.first(),
        onClickToUpdate = {},
        onNavigateBackToDetails = {}
    )
}