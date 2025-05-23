package br.com.arml.cep.ui.screen.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.ui.theme.dimens

@Composable
fun DisplayScreenPortrait(
    modifier: Modifier = Modifier,
    address: Address
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddressMap(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.dimens.mediumSpacing)
        )
        AddressScreen(
            modifier = Modifier,
            address = address
        )
    }
}