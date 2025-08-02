package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ElevatedCard
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
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CachePlaceElement(
    modifier: Modifier = Modifier,
    place: PlaceEntry,
    onNavigateToDetail: (PlaceEntry) -> Unit,
    onFavoriteIconClick: (PlaceEntry) -> Unit,
    onDeleteIconClick: (PlaceEntry) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ElevatedCard(
            modifier = modifier,
            shape = MaterialTheme.shapes.small
        ) {
            IconButton( onClick = { onDeleteIconClick(place) } ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.smallSpacing))
        PlaceElement(
            modifier = Modifier.padding(start = MaterialTheme.dimens.smallPadding),
            placeEntry = place,
            favoriteIcon = Icons.Default.FavoriteBorder,
            colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
            onNavigateToDetail = { place -> onNavigateToDetail(place) },
            onFavoriteIconClick = { place -> onFavoriteIconClick(place) }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun CachePlaceElementPreview() {
    CachePlaceElement(
        place = mockPlaceEntries.first(),
        onFavoriteIconClick = {},
        onNavigateToDetail = {},
        onDeleteIconClick = {}
    )
}