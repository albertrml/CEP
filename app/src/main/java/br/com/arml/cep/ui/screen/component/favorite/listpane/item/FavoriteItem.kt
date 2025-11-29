package br.com.arml.cep.ui.screen.component.favorite.listpane.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.note.NoteList
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteItem(
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
        FavoriteItemHeader(
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

@Preview(showBackground = true)
@Composable
fun FavoriteItemPreview() {
    Surface {
        FavoriteItem(
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
