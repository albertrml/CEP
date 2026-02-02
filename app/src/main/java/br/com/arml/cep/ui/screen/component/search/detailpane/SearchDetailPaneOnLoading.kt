package br.com.arml.cep.ui.screen.component.search.detailpane

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R.string.searchDetailPaneOnLoading_component_testTag
import br.com.arml.cep.R.string.searchDetailPaneOnLoading_loading_testTag

@Composable
fun SearchDetailPaneOnLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag(stringResource(searchDetailPaneOnLoading_component_testTag)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .testTag(stringResource(searchDetailPaneOnLoading_loading_testTag))
        )
    }
}