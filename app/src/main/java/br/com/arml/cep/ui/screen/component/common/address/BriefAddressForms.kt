package br.com.arml.cep.ui.screen.component.common.address

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.mapOfFields
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.ui.theme.dimens

@Composable
fun BriefAddressForms(
    modifier: Modifier = Modifier,
    address: Address,
    onClickToDetail: () -> Unit = {}
) {

    val mapFields = address
        .mapOfFields()
        .map { stringResource(it.key) to it.value }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = MaterialTheme.dimens.mediumThickness,
                color = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.large
            )
            .padding(MaterialTheme.dimens.largePadding)
            .clickable(onClick = onClickToDetail)
            .testTag(stringResource(R.string.addressForms_component_testTag)),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
    ) {
        mapFields.forEach { (field, value) ->
            if(
                field == stringResource(R.string.address_zipcodeField_label) ||
                field == stringResource(R.string.address_cityField_label) ||
                field == stringResource(R.string.address_stateField_label) ||
                field == stringResource(R.string.address_streetField_label)
            )
            AddressField(title = field, value = value)
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
@Composable
fun BriefAddressScreenPreview() {
    BriefAddressForms(
        modifier = Modifier.padding(MaterialTheme.dimens.mediumPadding),
        address = mockAddress(0)
    )
}