package br.com.arml.cep.ui.screen.component.search.detailpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R.string.searchDetailPaneOnSuccess_component_testTag
import br.com.arml.cep.R.string.searchDetailPaneOnSuccess_saveButton_description
import br.com.arml.cep.R.string.searchDetailPaneOnSuccess_saveButton_favoriteLabel
import br.com.arml.cep.R.string.searchDetailPaneOnSuccess_saveButton_testTag
import br.com.arml.cep.R.string.searchDetailPaneOnSuccess_saveButton_unfavoriteLabel
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.ui.screen.component.common.address.AddressForms
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CepSearchDetailPaneOnSuccess(
    modifier: Modifier = Modifier,
    place: Place,
    onFavoriteClick: (Place) -> Unit
) {

    val (colorIcon, textButton) = when (place.isFavorite.value) {
        true -> {
            Color.Red to stringResource(searchDetailPaneOnSuccess_saveButton_favoriteLabel)
        }

        false -> {
            MaterialTheme.colorScheme.onPrimary to stringResource(
                searchDetailPaneOnSuccess_saveButton_unfavoriteLabel
            )
        }
    }

    Column(
        modifier = modifier
            .testTag(stringResource(searchDetailPaneOnSuccess_component_testTag)),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .testTag(stringResource(searchDetailPaneOnSuccess_saveButton_testTag)),
            enabled = !place.isFavorite.value,
            onClick = { onFavoriteClick(place) }
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = stringResource(searchDetailPaneOnSuccess_saveButton_description),
                tint = colorIcon
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallPadding))
            Text(
                text = textButton,
                style = MaterialTheme.typography.titleMedium
            )
        }
        AddressForms(address = place.address)
    }
}