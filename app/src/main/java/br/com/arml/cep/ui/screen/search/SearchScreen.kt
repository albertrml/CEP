package br.com.arml.cep.ui.screen.search

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.screen.component.search.CepField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onSearchCep: (String) -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current // Obtenha o controlador

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Header(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.search_title),
            backImgVec = Icons.Default.Search
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CepField(
                modifier = Modifier,
                onQueryChange = { query = it }
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallSpacing))
            Button(
                modifier = Modifier,
                onClick = {
                    keyboardController?.hide()
                    onSearchCep(query)
                }
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
    SearchScreen(modifier = Modifier.fillMaxSize())
}