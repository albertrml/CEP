package br.com.arml.cep.ui.screen.component.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.ui.screen.component.common.field.SearchCepField
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.theme.dimens

@Composable
fun SearchListPane(
    modifier: Modifier = Modifier,
    onSearchCep: (String) -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    val isActive by remember { derivedStateOf { Cep.isValid(query) } }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            modifier = Modifier
                .testTag(
                    stringResource(R.string.testTag_searchScreen_listPane_header)
                ),
            title = stringResource(R.string.search_title),
            logo = Icons.Default.Search
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SearchCepField(
                modifier = Modifier
                    .testTag(
                        stringResource(R.string.testTag_searchScreen_listPane_cepField)
                    ),
                onQueryChange = { query = it }
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing))
            Button(
                modifier = Modifier
                    .testTag(stringResource(
                        R.string.testTag_searchScreen_listPane_searchButton)
                    ),
                onClick = {
                    keyboardController?.hide()
                    onSearchCep(query)
                },
                enabled = isActive
            ) {
                Text(text = stringResource(R.string.search_button_label))
            }
        }
    }
}

@Preview(
    name = "Smart Phone Portrait",
    showBackground = true,
    device = "spec:width=360dp,height=640dp"
)
@Preview(
    name = "Compact Phone Portrait",
    showBackground = true,
    device = "spec:width=412dp,height=924dp"
)
@Preview(
    name = "Medium Tablet Portrait",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp"
)
@Preview(
    name = "Smart Phone Landscape",
    showBackground = true,
    device = "spec:width=640dp,height=360dp"
)
@Preview(
    name = "Compact Phone Landscape",
    showBackground = true,
    device = "spec:width=924dp,height=412dp"
)
@Preview(
    name = "Medium Tablet Landscape",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp"
)
@Composable
fun SearchScreenPreview() {
    SearchListPane(modifier = Modifier.fillMaxSize())
}