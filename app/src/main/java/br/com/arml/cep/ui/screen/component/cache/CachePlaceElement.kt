package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    favoriteIcon: ImageVector,
    colorFavoriteIcon: Color,
    onDeletePlace: (Place) -> Unit,
    onFavoriteIconClick: (Place) -> Unit,
    onNavigateToDetail: (Place) -> Unit
) {
    Row(
        modifier = modifier
            .border(
                width = MaterialTheme.dimens.smallThickness,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(MaterialTheme.dimens.mediumPadding)
            .combinedClickable(
                onClick = { onNavigateToDetail(place) },
                onLongClick = { onDeletePlace(place) }
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(
                R.string.favorite_place_element_zipcode,
                place.address.zipCode
            ),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { onFavoriteIconClick(place) },
        ) {
            Icon(
                imageVector = favoriteIcon,
                contentDescription = null,
                tint = colorFavoriteIcon
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CachePlaceElementPreview() {
    CachePlaceElement(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        place = mockUnfavoritePlaces.first(),
        favoriteIcon = Icons.Default.FavoriteBorder,
        colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
        onDeletePlace = {},
        onFavoriteIconClick = {},
        onNavigateToDetail = {}
    )
}