package br.com.arml.cep.ui.screen.component.favorite.listpane.item.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NoteAdd
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteListMenu(
    modifier: Modifier = Modifier,
    isShownNotes: Boolean,
    onChangeShownNotes: () -> Unit = {},
    onAddNote: () -> Unit = {}
){
    Row(
        modifier = modifier
            .testTag(stringResource(R.string.noteListMenu_component_testTag)),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NoteListVisibility(
            onChangeShownNotes = onChangeShownNotes,
            isShownNotes = isShownNotes
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.smallMargin)
                .testTag(stringResource(R.string.noteListMenu_horizontalDivider_testTag)),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .testTag(stringResource(R.string.noteListMenu_addNoteButton_testTag)),
            onClick = onAddNote,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.NoteAdd,
                contentDescription = stringResource(R.string.noteListMenu_noteAdd_description),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListMenuPreview(){
    var isShownNotes by remember { mutableStateOf(false) }
    NoteListMenu(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        isShownNotes = isShownNotes,
        onChangeShownNotes = { isShownNotes = !isShownNotes },
        onAddNote = {}
    )
}