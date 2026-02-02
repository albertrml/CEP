package br.com.arml.cep.ui.screen.component.common.field

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R

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
    showInputSize: Boolean = false
){
    Column {
        OutlinedTextField(
            modifier = modifier
                .testTag(stringResource(R.string.cepTextField_component_testTag)),
            value = text,
            onValueChange = { newText ->
                if (newText.length <= maxSize) {
                    onChangeText(newText)
                } else {
                    onChangeText(newText.take(maxSize))
                }
            },
            label = {
                Text(
                    text = nameField,
                    style = MaterialTheme.typography.labelLarge
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
        if(showInputSize){
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .testTag(stringResource(R.string.cepTextField_inputCounter_testTag)),
                text = "${text.length}/$maxSize",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CepTextFieldPreview(){
    CepTextField(
        nameField = "Name",
        text = "",
        onChangeText = { },
        maxSize = 100,
        maxLines = 3
    )
}