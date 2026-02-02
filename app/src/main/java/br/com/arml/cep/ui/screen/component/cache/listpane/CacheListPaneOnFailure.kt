package br.com.arml.cep.ui.screen.component.cache.listpane

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R

@Composable
fun CacheListPaneOnFailure(
    modifier: Modifier = Modifier,
    failureMessage: String,
){
    Text(
        modifier = modifier
            .testTag(stringResource(R.string.cacheListPaneOnFailure_component_testTag)),
        text = failureMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium
    )
}