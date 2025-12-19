package br.com.arml.cep.ui.screen.component.cache.listpane

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R

@Composable
fun CacheListPaneOnLoading(
    modifier: Modifier = Modifier
){
    CircularProgressIndicator(
        modifier = modifier
            .testTag(stringResource(R.string.cacheListPaneOnLoading_component_testTag))
    )
}