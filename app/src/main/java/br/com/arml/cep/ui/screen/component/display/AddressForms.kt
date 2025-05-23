package br.com.arml.cep.ui.screen.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R

import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.ui.theme.dimens

@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    address: Address
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.display_address_title),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.smallSpacing),
                thickness = MaterialTheme.dimens.mediumThickness,
                color = MaterialTheme.colorScheme.onBackground
            )
            AddressField(
                title = "CEP: ",
                value = address.zipCode
            )
            AddressField(title = stringResource(R.string.display_street_field), value = address.street)
            AddressField(title = stringResource(R.string.display_complement_field), value = address.complement)
            AddressField(title = stringResource(R.string.display_neighborhood_field), value = address.neighborhood)
            AddressField(title = stringResource(R.string.display_city_field), value = address.city)
            AddressField(title = stringResource(R.string.display_state_field), value = address.state)
            AddressField(title = stringResource(R.string.display_uf_field), value = address.uf)
            AddressField(title = stringResource(R.string.display_region_field), value = address.region)
            AddressField(title = stringResource(R.string.display_country_field), value = address.country)
            AddressField(title = stringResource(R.string.display_ddd_field), value = address.ddd)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.smallSpacing),
                thickness = MaterialTheme.dimens.mediumThickness,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}