package br.com.arml.cep.ui.screen.component.favorite.detailpane.note

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_CONTENT_LENGTH
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.common.field.AppTextField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteContentField(
    modifier: Modifier = Modifier,
    content: String,
    onContentChange: (String) -> Unit
){
    AppTextField(
        modifier = modifier
            .testTag(stringResource(R.string.noteContentField_component_testTag)),
        nameField = stringResource(R.string.noteContentField_nameField_text),
        text = content,
        onChangeText = { onContentChange(it) },
        maxSize = MAX_CONTENT_LENGTH,
        minLines = 7,
        maxLines = 10,
        showInputSize = true
    )
}

@Preview(showBackground = true)
@Composable
fun NoteContentFieldPreview(){
    NoteContentField(
        modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.smallPadding),
        content = mockNotes.first().content,
        onContentChange = {}
    )
}