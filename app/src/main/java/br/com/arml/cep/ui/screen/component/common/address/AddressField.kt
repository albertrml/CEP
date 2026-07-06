package br.com.arml.cep.ui.screen.component.common.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun AddressField(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Row(
        modifier = modifier
            .testTag(stringResource(R.string.addressField_component_testTag)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        Text(
            modifier = Modifier
                .testTag(stringResource(R.string.addressField_title_testTag)),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier
                .testTag(stringResource(R.string.addressField_value_testTag)),
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddressFieldPreview() {
    AddressField(
        title = "CEP",
        value = "01001-000"
    )
}