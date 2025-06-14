package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepTextField(
    modifier: Modifier = Modifier,
    nameField: String,
    text: String,
    onChangeText: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    isError: Boolean = false,
    errorMessage: String = "",
    minLines: Int = 1,
    maxSize: Int,
    maxLines: Int = 1,
){

    var currentTextSize by rememberSaveable { mutableIntStateOf(text.length) }

    Column {
        OutlinedTextField(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            value = text,
            onValueChange = { newText ->
                if (newText.length <= maxSize) {
                    onChangeText(newText)
                    currentTextSize = newText.length
                }
            },
            label = {
                Text(
                    text = nameField,
                    style = textStyle
                )
            },
            textStyle = textStyle,
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background
            ),
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing))
        Text(
            modifier = Modifier
                .align(Alignment.End),
            text = "$currentTextSize/$maxSize",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CepTextFieldPreview(){
    var text by rememberSaveable { mutableStateOf("") }
    CepTextField(
        modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing),
        nameField = "Name",
        text = text,
        onChangeText = { newText -> text = newText },
        maxSize = 100,
        maxLines = 3
    )
}