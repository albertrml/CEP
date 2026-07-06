package br.com.arml.cep.ui.screen.component.favorite.listpane.item.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteList(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onDeleteNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit
) {
    Column(
        modifier = modifier
            .testTag(stringResource(R.string.noteList_component_testTag)),
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.mediumSpacing),
    ) {
        notes.forEach { note ->
            NoteItem(
                note = note,
                onDeleteNote = { onDeleteNote(it) },
                onEditNote = { onEditNote(it) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x222222FF)
@Composable
fun NoteListPreview(){
    NoteList(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        notes = mockFavoritePlaces.first().notes,
        onDeleteNote = {},
        onEditNote = {}
    )
}