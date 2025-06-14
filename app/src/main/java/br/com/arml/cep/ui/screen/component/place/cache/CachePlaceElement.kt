package br.com.arml.cep.ui.screen.component.place.cache

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.place.PlaceElement

@Composable
fun CachePlaceElement(
    modifier: Modifier = Modifier,
    place: PlaceEntry,
    onFavoriteIconClick: (PlaceEntry) -> Unit,
    onDeleteIconClick: (PlaceEntry) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onDeleteIconClick(place) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }

            PlaceElement(
                modifier = Modifier,
                placeEntry = place,
                favoriteIcon = Icons.Default.FavoriteBorder,
                colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
                onFavoriteIconClick = { place -> onFavoriteIconClick(place) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CachePlaceElementPreview() {
    CachePlaceElement(
        place = mockPlaceEntries.first(),
        onFavoriteIconClick = {},
        onDeleteIconClick = {}
    )
}