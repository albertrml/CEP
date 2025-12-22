package br.com.arml.cep.ui.screen.component.log.listpane

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R

@Composable
fun LogListPaneOnFailure(
    modifier: Modifier = Modifier,
    failureMsg: String
){
    Text(
        modifier = modifier
            .testTag(stringResource(R.string.logListPaneOnFailure_component_testTag)),
        text = failureMsg,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
}