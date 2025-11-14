package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
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
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.theme.dimens

@Composable
fun PlaceElement(
    modifier: Modifier = Modifier,
    place: Place,
    favoriteIcon: ImageVector,
    colorFavoriteIcon: Color,
    onNavigateToDetail: (Place) -> Unit = {},
    onFavoriteIconClick: (Place) -> Unit = {},
) {
    var isShownNotes by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .border(
                width = MaterialTheme.dimens.smallThickness,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .padding(
                vertical = MaterialTheme.dimens.smallPadding,
                horizontal = MaterialTheme.dimens.mediumPadding
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        FavoritePlaceElement(
            place = place,
            favoriteIcon = favoriteIcon,
            colorFavoriteIcon = colorFavoriteIcon,
            onFavoriteIconClick = onFavoriteIconClick
        )

        NoteComponent(
            place = place,
            isShownNotes = isShownNotes,
            onChangeShownNotes = { isShownNotes = !isShownNotes }
        )

        if (isShownNotes) {
            NoteList(notes = place.notes)
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
            style = MaterialTheme.typography.bodyMedium
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

@Composable
fun NoteComponent(
    modifier: Modifier = Modifier,
    place: Place,
    isShownNotes: Boolean,
    onChangeShownNotes: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.smallMargin),
        )
        Text(
            modifier = Modifier.clickable { onChangeShownNotes() },
            text = if (isShownNotes) "Esconder Anotações" else "Mostrar Anotações"
        )
        Icon(
            imageVector = if (isShownNotes) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.smallMargin),
        )
    }
}

@Composable
fun NoteList(
    modifier: Modifier = Modifier,
    notes: List<Note>,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
    ) {
        items(notes) { note ->
            NoteElement(
                modifier = modifier,
                note = note,
                onDeleteNote = {},
                onEditNote = {}
            )
        }
    }
}

@Composable
fun NoteElement(
    modifier: Modifier = Modifier,
    note: Note,
    onDeleteNote: (Note) -> Unit = {},
    onEditNote: (Note) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .clickable { onEditNote(note) },
        shape = MaterialTheme.shapes.medium,
    ){
        Row(
            modifier = modifier
                .padding(MaterialTheme.dimens.smallMargin),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onDeleteNote(note) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnwantedPlaceElementPreview() {
    Surface {
        PlaceElement(
            place = mockUnfavoritePlaces.first(),
            colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
            favoriteIcon = Icons.Default.FavoriteBorder,
            onNavigateToDetail = {},
            onFavoriteIconClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePlaceElementPreview() {
    Surface {
        PlaceElement(
            modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
            place = mockFavoritePlaces.first(),
            favoriteIcon = Icons.Default.Favorite,
            colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
            onNavigateToDetail = {},
            onFavoriteIconClick = {}
        )
    }
}