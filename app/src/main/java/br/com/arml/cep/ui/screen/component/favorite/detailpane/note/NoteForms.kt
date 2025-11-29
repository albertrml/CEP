package br.com.arml.cep.ui.screen.component.favorite.detailpane.note


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteForms(
    modifier: Modifier = Modifier,
    note: Note?,
    onClick: (Note) -> Unit
) {
    val id by rememberSaveable { mutableLongStateOf(note?.id ?: 0) }
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(note?.content ?: "") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        NoteTitleField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.testTag_favoriteExtraScreen_titleNoteField)),
            title = title,
            onTitleChange = { title = it }
        )

        NoteContentField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.testTag_favoriteExtraScreen_contentNoteField)),
            content = content,
            onContentChange = { content = it }
        )

        NoteFormsButton(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_favoriteExtraScreen_updateButton)),
            id = id,
            title = title,
            content = content,
            onClick = { note -> onClick(note) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteFormsPreview() {
    NoteForms(
        modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing),
        note = mockNotes.first(),
        onClick = {}
    )
}