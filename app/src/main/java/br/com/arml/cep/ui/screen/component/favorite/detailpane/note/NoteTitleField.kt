package br.com.arml.cep.ui.screen.component.favorite.detailpane.note

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.model.domain.isValidTitleNoteSize
import br.com.arml.cep.ui.screen.component.common.field.AppTextField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteTitleField(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit
){
    AppTextField(
        modifier = modifier,
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
}

@Preview(showBackground = true)
@Composable
fun NoteTitleFieldPreview(){
    NoteTitleField(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        title = "teste",
        onTitleChange = {}
    )
}