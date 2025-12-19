package br.com.arml.cep.ui.screen.component.cache.listpane.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CachePlaceElement(
    modifier: Modifier = Modifier,
    place: Place,
    onDeletePlace: (Place) -> Unit,
    onNavigateToDetail: (Place) -> Unit
) {
    Surface(
        modifier = modifier
            .clickable { onNavigateToDetail(place) }
            .testTag(
                stringResource(
                    R.string.cachePlaceElement_component_testTag,
                    place.toString()
                )
            ),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        border = BorderStroke(
            width = MaterialTheme.dimens.smallThickness,
            color = MaterialTheme.colorScheme.outline,
        )
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimens.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(
                    R.string.place_zipcode_text,
                    place.address.zipCode
                ),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = Modifier
                    .testTag(
                        stringResource(
                            R.string.cachePlaceElement_deleteIcon_testTag,
                            place.address.zipCode
                        )
                    ),
                onClick = { onDeletePlace(place) },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(
                        R.string.cachePlaceElement_deleteIcon_description,
                        place.address.zipCode
                    ),
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CachePlaceElementPreview() {
    CachePlaceElement(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        place = mockUnfavoritePlaces.first(),
        onDeletePlace = {},
        onNavigateToDetail = {}
    )
}