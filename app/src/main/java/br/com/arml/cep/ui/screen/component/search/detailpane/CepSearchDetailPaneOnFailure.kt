package br.com.arml.cep.ui.screen.component.search.detailpane

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R.string.searchDetailPaneOnFailure_component_testTag
import br.com.arml.cep.model.exception.CepException

@Composable
fun CepSearchDetailPaneOnFailure(
    modifier: Modifier = Modifier,
    failure: Exception
) {
    Box(
        modifier = modifier
            .testTag(stringResource(searchDetailPaneOnFailure_component_testTag)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = failure.message ?: CepException.NotFoundCepException().message
        )
    }
}