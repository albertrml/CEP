package br.com.arml.cep.ui.screen.component.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.ui.screen.component.common.ScrollableFab
import br.com.arml.cep.ui.theme.dimens

@Composable
fun AddressForms(
    modifier: Modifier = Modifier,
    address: Address,
) {
    ScrollableFab(
        modifier = modifier,
    ) { scrollState ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
        ) {
            AddressField(
                modifier = Modifier.padding(
                    top = MaterialTheme.dimens.mediumPadding,
                    bottom = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_zipcode_field),
                value = address.zipCode
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_street_field),
                value = address.street
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_complement_field),
                value = address.complement
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_neighborhood_field),
                value = address.district
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_city_field),
                value = address.city
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_state_field),
                value = address.state
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_uf_field),
                value = address.uf
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_region_field),
                value = address.region
            )
            AddressField(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.xSmallPadding
                ),
                title = stringResource(R.string.display_country_field),
                value = address.country
            )
            AddressField(
                modifier = Modifier.padding(
                    bottom = MaterialTheme.dimens.mediumPadding,
                    top = MaterialTheme.dimens.xSmallPadding,
                ),
                title = stringResource(R.string.display_ddd_field),
                value = address.ddd,
                thickness = MaterialTheme.dimens.largeThickness
            )
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
fun AddressScreenPreview() {
    AddressForms(
        modifier = Modifier.fillMaxSize(),
        address = mockAddress
    )
}