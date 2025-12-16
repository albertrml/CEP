package br.com.arml.cep.ui.screen.component.favorite.listpane.item.note

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onDeleteNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .border(
                width = MaterialTheme.dimens.smallThickness,
                color = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onEditNote(note) }
            .testTag(
                stringResource(
                    R.string.noteItem_component_testTag,
                    note.id
                )
            ),
        shape = MaterialTheme.shapes.medium,
    ){
        Row(
            modifier = Modifier
                .padding(MaterialTheme.dimens.smallMargin),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .testTag(stringResource(R.string.noteItem_titleText_testTag)),
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag(stringResource(R.string.noteItem_deleteIconButton_testTag)),
                onClick = { onDeleteNote(note) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(
                        R.string.noteItem_deleteIconButton_description,
                        note.title
                    ),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItemPreview(){
    val notes = mockFavoritePlaces.first().notes
    NoteItem(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        note = notes.first(),
        onDeleteNote = {},
        onEditNote = {}
    )
}
