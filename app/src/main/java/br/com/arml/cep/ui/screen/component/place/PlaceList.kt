package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.common.ScrollableFab
import br.com.arml.cep.ui.theme.dimens

@Composable
fun PlaceList(
    modifier: Modifier = Modifier,
    placeEntries: List<PlaceEntry>,
    logo: ImageVector,
    colorLogo: Color,
    onClickToDelete: ((PlaceEntry) -> Unit)? = null,
    onClickLogo: (PlaceEntry) -> Unit,
    onClickEntry: (PlaceEntry) -> Unit
) {

    val lazyListState = rememberLazyListState()

    ScrollableFab(listState = lazyListState) {
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            items(placeEntries) { placeEntry ->

                Row(verticalAlignment = Alignment.CenterVertically) {
                    onClickToDelete?.let {
                        IconButton(
                            onClick = { it(placeEntry) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.clickable(
                            onClick = { onClickEntry(placeEntry) }
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        PlaceElement(
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.dimens.smallPadding)
                                .padding(start = MaterialTheme.dimens.mediumPadding),
                            placeEntry = placeEntry,
                            onClickLogo = onClickLogo,
                            logo = logo,
                            colorLogo = colorLogo
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FavoriteListPreview() {
    PlaceList(
        placeEntries = mockPlaceEntries,
        logo = Icons.Default.Favorite,
        colorLogo = MaterialTheme.colorScheme.onSurface,
        onClickLogo = {},
        onClickEntry = {}
    )
}