package br.com.arml.cep.ui.screen.component.search.detailpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.screen.component.common.address.BriefAddressForms
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollGrid
import br.com.arml.cep.ui.screen.component.common.field.CepSearchField
import br.com.arml.cep.ui.theme.dimens

@Composable
fun AddressSearchDetailPaneOnSuccess(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onNavigateToAddress: (Place) -> Unit
){
    var query by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding)
        ) {
            Icon(
                modifier = Modifier
                    .size(MaterialTheme.dimens.mediumIconSize)
                    .padding(end = MaterialTheme.dimens.smallPadding),
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.cepSearchField_hint)
            )
            CepSearchField(
                modifier = Modifier.weight(1f),
                onQueryChange = { query = it }
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.mediumPadding)
        )
        if(!places.isEmpty()) {
            FastScrollGrid {
                val currentPlaces = places.filter { it.address.zipCode.contains(query) }
                items(currentPlaces) { place ->
                    BriefAddressForms(
                        modifier = Modifier.padding(MaterialTheme.dimens.xSmallPadding),
                        address = place.address,
                        onClickToDetail = { onNavigateToAddress(place) }
                    )
                }
            }
        }
    }
}