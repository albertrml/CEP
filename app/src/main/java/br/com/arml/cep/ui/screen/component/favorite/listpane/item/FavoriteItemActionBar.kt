package br.com.arml.cep.ui.screen.component.favorite.listpane.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteItemHeader(
    modifier: Modifier = Modifier,
    place: Place,
    favoriteIcon: ImageVector,
    colorFavoriteIcon: Color,
    onFavoriteIconClick: (Place) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
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
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .testTag(
                    stringResource(
                        R.string.testTag_placeElement_favoriteIconButton,
                        place.cep.text
                    )
                ),
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
fun FavoriteItemHeaderPreview() {
    Surface {
        FavoriteItemHeader(
            modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
            place = mockFavoritePlaces.first(),
            favoriteIcon = Icons.Default.Favorite,
            colorFavoriteIcon = MaterialTheme.colorScheme.onSurface
        )
    }
}