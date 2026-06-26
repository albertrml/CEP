package br.com.arml.cep.ui.screen.component.search.listpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.ui.screen.component.common.field.CepSearchField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepSearchComponent(
    modifier: Modifier = Modifier,
    onSearchCep: (String) -> Unit = {}
){
    var query by rememberSaveable { mutableStateOf("") }
    val isActive by remember { derivedStateOf { Cep.isValid(query) } }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        CepSearchField(
            modifier = Modifier.fillMaxWidth(),
            onQueryChange = { query = it }
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.mediumSpacing))
        Button(
            modifier = Modifier.testTag(
                stringResource(R.string.cepSearchComponent_searchButton_testTag)
            ),
            onClick = {
                keyboardController?.hide()
                onSearchCep(query)
            },
            enabled = isActive
        ) {
            Text(text = stringResource(R.string.cepSearchComponent_searchButton_label))
        }
    }
}