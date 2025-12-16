package br.com.arml.cep.ui.screen.component.favorite.listpane.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
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
fun FavoriteItemActionBar(
    modifier: Modifier = Modifier,
    place: Place,
    favoriteIcon: ImageVector,
    colorFavoriteIcon: Color,
    onFavoriteChanges: (Place) -> Unit = {},
) {
    Row(
        modifier = modifier
            .testTag(stringResource(R.string.favoriteItemActionBar_component_testTag)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(
                R.string.favoriteItemActionBar_titleText_zipcode,
                place.cep.text
            ),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .testTag(
                    stringResource(
                        R.string.favoriteItemActionBar_favoriteIcon_testTag,
                        place.cep.text
                    )
                ),
            onClick = { onFavoriteChanges(place) },
        ) {
            Icon(
                imageVector = favoriteIcon,
                contentDescription = stringResource(
                    R.string.favoriteItemActionBar_favoriteIcon_description
                ),
                tint = colorFavoriteIcon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteItemActionBarPreview() {
    Surface {
        FavoriteItemActionBar(
            modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
            place = mockFavoritePlaces.first(),
            favoriteIcon = Icons.Default.Favorite,
            colorFavoriteIcon = MaterialTheme.colorScheme.onSurface
        )
    }
}