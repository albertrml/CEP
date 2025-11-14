package br.com.arml.cep.ui.screen.component.favorite


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import br.com.arml.cep.model.domain.MAX_CONTENT_LENGTH
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.isValidTitleNoteSize
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.common.CepTextField
import br.com.arml.cep.ui.screen.component.favorite.component.FavoriteUpdateButton
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteNotesComponent(
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
        NoteComponent(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.dimens.mediumSpacing),
            id = id,
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            onClick = { note -> onClick(note) }
        )
    }
}

@Composable
fun NoteComponent(
    modifier: Modifier = Modifier,
    id: Long,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onClick: (Note) -> Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        CepTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.testTag_favoriteExtraScreen_titleNoteField)),
            nameField = stringResource(R.string.place_details_title),
            text = title,
            textStyle = MaterialTheme.typography.titleMedium,
            onChangeText = { onTitleChange(it) },
            maxSize = MAX_TITLE_LENGTH,
            isError = !title.isValidTitleNoteSize(),
            errorMessage = stringResource(
                R.string.favorite_details_title_error_msg,
                MIN_TITLE_LENGTH,
                MAX_TITLE_LENGTH
            ),
            showInputSize = true
        )

        CepTextField(
            modifier = Modifier.fillMaxWidth()
                .testTag(stringResource(R.string.testTag_favoriteExtraScreen_contentNoteField)),
            nameField = stringResource(R.string.place_details_content),
            text = content,
            onChangeText = { onContentChange(it) },
            maxSize = MAX_CONTENT_LENGTH,
            minLines = 7,
            maxLines = 10,
            showInputSize = true
        )

        FavoriteUpdateButton(
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
fun FavoriteNotesComponentPreview() {
    FavoriteNotesComponent(
        modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing),
        note = mockNotes.first(),
        onClick = {}
    )
}
