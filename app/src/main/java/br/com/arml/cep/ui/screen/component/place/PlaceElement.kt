package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.theme.dimens

@Composable
fun PlaceElement(
    modifier: Modifier = Modifier,
    placeEntry: PlaceEntry,
    logo: ImageVector,
    colorLogo: Color,
    onClickLogo: (PlaceEntry) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
        ) {
            placeEntry.note?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = stringResource(
                    R.string.favorite_place_element_zipcode,
                    placeEntry.address.zipCode
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { onClickLogo(placeEntry) },
        ) {
            Icon(
                imageVector = logo,
                contentDescription = null,
                tint = colorLogo
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnwantedPlaceElementPreview() {
    Surface {
        PlaceElement(
            placeEntry = mockPlaceEntries.first(),
            colorLogo = MaterialTheme.colorScheme.onSurface,
            logo = Icons.Default.FavoriteBorder,
            onClickLogo = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePlaceElementPreview() {
    Surface {
        PlaceElement(
            placeEntry = mockPlaceEntries.first().copy(
                isFavorite = Favorite(true),
                note = Note.build(
                    title = stringResource(R.string.app_name),
                    content = stringResource(R.string.app_name)
                )
            ),
            logo = Icons.Default.Favorite,
            colorLogo = MaterialTheme.colorScheme.onSurface,
            onClickLogo = {}
        )
    }
}