package br.com.arml.cep.ui.screen.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.ui.theme.dimens

@Composable
fun DisplayScreenLandscape(
    modifier: Modifier = Modifier,
    address: Address
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ){
        AddressMap(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.dimens.mediumSpacing)
        )
        AddressScreen(
            modifier = Modifier.weight(1f),
            address = address
        )
    }
}