package br.com.arml.cep.ui.screen.component.common.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.CEP_LENGTH
import br.com.arml.cep.model.domain.MIN_CEP_LENGTH_FOR_SEARCH
import br.com.arml.cep.ui.screen.component.common.field.SearchCepField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepFilter(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit
) {
    var zipcode by remember { mutableStateOf("") }
    val isButtonActive by remember {
        derivedStateOf { zipcode.length in MIN_CEP_LENGTH_FOR_SEARCH..CEP_LENGTH }
    }

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.cepFilter_component_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        SearchCepField(onQueryChange = { zipcode = it })
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.cepFilter_filterButton_testTag)),
            enabled = isButtonActive,
            onClick = { onFilterByCep(zipcode) },
        ) { Text(text = stringResource(R.string.cepFilter_filterButton_label)) }
    }
}

@Preview(showBackground = true)
@Composable
fun CepFilterPreview() { CepFilter(onFilterByCep = {}) }