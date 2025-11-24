package br.com.arml.cep.ui.screen.component.favorite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteElement(
    modifier: Modifier = Modifier,
    place: Place,
    favoriteIcon: ImageVector,
    colorFavoriteIcon: Color,
    onFavoriteIconClick: (Place) -> Unit,
    onAddNote: (Cep, Note) -> Unit,
    onDeleteNote: (Pair<Cep, Note>) -> Unit,
    onNavigateToDetail: (Pair<Address, Note>) -> Unit
) {
    var isShownNotes by rememberSaveable { mutableStateOf(false) }

    Column(
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
            .padding(MaterialTheme.dimens.mediumPadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        FavoritePlaceElement(
            place = place,
            favoriteIcon = favoriteIcon,
            colorFavoriteIcon = colorFavoriteIcon,
            onFavoriteIconClick = onFavoriteIconClick
        )

        FavoriteDivider(
            isShownNotes = isShownNotes,
            onChangeShownNotes = { isShownNotes = !isShownNotes },
            onAddNote = {
                onAddNote(
                    place.cep,
                    Note.build(title = place.cep.text, content = "")
                )
            }
        )

        if (isShownNotes) {
            NoteList(
                notes = place.notes,
                onDeleteNote = { note -> onDeleteNote(place.cep to note) },
                onEditNote = { note -> onNavigateToDetail(place.address to note) }
            )
        }
    }
}

@Composable
fun FavoritePlaceElement(
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
fun FavoritePlaceElementPreview() {
    Surface {
        FavoriteElement(
            modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
            place = mockFavoritePlaces.first(),
            favoriteIcon = Icons.Default.Favorite,
            colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
            onFavoriteIconClick = {},
            onAddNote = { _, _ -> },
            onDeleteNote = {},
            onNavigateToDetail = {},
        )
    }
}
