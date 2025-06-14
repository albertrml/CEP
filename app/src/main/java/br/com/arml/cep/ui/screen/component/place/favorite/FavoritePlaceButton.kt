package br.com.arml.cep.ui.screen.component.place.favorite

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.isValidTitleNoteSize
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoritePlaceUpdateButton(
    modifier: Modifier = Modifier,
    place: PlaceEntry,
    title: String,
    note: String,
    onClickToUpdate: (PlaceEntry) -> Unit
){
    Button(
        modifier = modifier,
        enabled = title.isValidTitleNoteSize(),
        onClick = {
            val newNote = Note.build(
                title = title,
                content = note
            )
            onClickToUpdate(place.copy(note = newNote))
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