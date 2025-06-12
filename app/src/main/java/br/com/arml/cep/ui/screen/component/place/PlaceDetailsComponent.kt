package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.isValidTitleNoteSize
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockFavoritePlaceEntries
import br.com.arml.cep.ui.screen.component.cep.display.AddressScreen
import br.com.arml.cep.ui.screen.component.common.CepTextField
import br.com.arml.cep.ui.screen.component.common.Header
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
        Header(
            modifier = modifier,
            logo = Icons.AutoMirrored.Filled.ArrowBack,
            title = stringResource(R.string.favorite_details_title),
            onClickLogo = onNavigateBack
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing))
        CepTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.smallSpacing),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.smallSpacing),
            nameField = stringResource(R.string.place_details_content),
            text = note,
            onChangeText = { note = it },
            maxSize = MAX_CONTENT_LENGTH,
            maxLines = 7
        )
        AddressScreen(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(MaterialTheme.dimens.smallSpacing),
            address = placeEntry.address
        )
        Button(
            modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing),
            enabled = title.isValidTitleNoteSize(),
            onClick = {
                val newNote = Note.build(
                    title = title,
                    content = note
                )
                onClickToUpdate(placeEntry.copy(note = newNote))
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = stringResource(R.string.favorite_details_update_button_description)
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing))
            Text(text = stringResource(R.string.favorite_details_update_button))
        }
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