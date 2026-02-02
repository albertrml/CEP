package br.com.arml.cep.ui.screen.component.favorite.listpane.item.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteListComponent(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onDeleteNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit,
    onAddNote: () -> Unit
) {

    var isShownNotes by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.noteListComponent_component_testTag))
    ) {
        NoteListMenu(
            isShownNotes = isShownNotes,
            onChangeShownNotes = { isShownNotes = !isShownNotes },
            onAddNote = onAddNote
        )
        if (isShownNotes) {
            NoteList(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.smallMargin),
                notes = notes,
                onDeleteNote = onDeleteNote,
                onEditNote = onEditNote
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoteListComponentPreview(){
    NoteListComponent(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        notes = mockNotes,
        onDeleteNote = {},
        onEditNote = {},
        onAddNote = {}
    )
}