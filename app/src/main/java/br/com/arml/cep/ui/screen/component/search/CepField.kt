package br.com.arml.cep.ui.screen.component.search

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.Cep
import br.com.arml.cep.model.entity.updateCepField

@Composable
fun CepField(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit
){
    var cepFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    OutlinedTextField(
        modifier = modifier.semantics{
            contentType = ContentType.PostalCode
        },
        value = cepFieldValue,
        onValueChange = { newFieldValue ->
            val digitsOnly = updateCepField(
                oldValue = cepFieldValue.text,
                newValue = newFieldValue.text
            )
            onQueryChange(digitsOnly)
            cepFieldValue = newFieldValue.copy(
                text = Cep.format(digitsOnly),
                selection = TextRange(Cep.format(digitsOnly).length)
            )
        },
        label = {
            Text(
                text = stringResource(R.string.search_cep_field_name),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search_cep_hint),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = {
            if(cepFieldValue.text.isNotEmpty()){
                IconButton(
                    onClick = {
                        cepFieldValue = TextFieldValue("")
                        onQueryChange(cepFieldValue.text)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.search_cep_clear_button)
                    )
                }
            }

        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Preview (showBackground = true)
@Composable
fun CepFieldPreview(){
    CepField(onQueryChange = {})
}