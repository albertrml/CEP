package br.com.arml.cep.ui.screen.component.favorite.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.theme.dimens
import kotlin.collections.forEach

@Composable
fun NoteList(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onDeleteNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.mediumSpacing),
    ) {
        notes.forEach { note ->
            NoteElement(
                modifier = modifier,
                note = note,
                onDeleteNote = { onDeleteNote(it) },
                onEditNote = { onEditNote(it) }
            )
        }
    }
}

@Composable
fun NoteElement(
    modifier: Modifier = Modifier,
    note: Note,
    onDeleteNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .clickable { onEditNote(note) },
        shape = MaterialTheme.shapes.medium,
    ){
        Row(
            modifier = modifier
                .padding(MaterialTheme.dimens.smallMargin)
                .clickable { onEditNote(note) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onDeleteNote(note) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview(){
    NoteList(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        notes = mockFavoritePlaces.first().notes,
        onDeleteNote = {},
        onEditNote = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NoteElementPreview(){
    val notes = mockFavoritePlaces.first().notes
    NoteElement(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        note = notes.first(),
        onDeleteNote = {},
        onEditNote = {}
    )
}
