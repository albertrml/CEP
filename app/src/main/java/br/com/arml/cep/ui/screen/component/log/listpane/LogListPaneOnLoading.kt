package br.com.arml.cep.ui.screen.component.log.listpane

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R

@Composable
fun LogListPaneOnLoading(
    modifier: Modifier = Modifier,
){
    CircularProgressIndicator(
        modifier = modifier.testTag(stringResource(R.string.logListPaneOnLoading_component_testTag))
    )
}