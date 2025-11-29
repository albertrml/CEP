package br.com.arml.cep.ui.screen.component.favorite.detailpane.note

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
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.isValidTitleNoteSize
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteFormsButton(
    modifier: Modifier = Modifier,
    id: Long,
    title: String,
    content: String,
    onClick: (Note) -> Unit
){
    val textButton = if(id == 0L)
        stringResource(R.string.favorite_details_save_button)
    else
        stringResource(R.string.favorite_details_update_button)

    Button(
        modifier = modifier,
        enabled = title.isValidTitleNoteSize(),
        onClick = {
            val newNote = Note.build(
                id = id,
                title = title,
                content = content
            )
            onClick(newNote)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Create,
            contentDescription = stringResource(R.string.favorite_details_update_button_description)
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing))
        Text(text = textButton)
    }
}

@Preview(showBackground = true)
@Composable
fun NoteFormsButtonPreview() {
    NoteFormsButton(
        id = 1,
        title = "teste",
        content = "teste",
        onClick = {}
    )
}