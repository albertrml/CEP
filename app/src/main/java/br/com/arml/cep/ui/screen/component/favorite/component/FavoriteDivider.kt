package br.com.arml.cep.ui.screen.component.favorite.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.rounded.NoteAdd
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteDivider(
    modifier: Modifier = Modifier,
    isShownNotes: Boolean,
    onChangeShownNotes: () -> Unit = {},
    onAddNote: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable { onChangeShownNotes() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (isShownNotes)
                    stringResource(id = R.string.favorite_notes_hide_all_button)
                else
                    stringResource(id = R.string.favorite_notes_show_all_button),
            )
            Icon(
                imageVector = if (isShownNotes) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.smallMargin),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = onAddNote,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.NoteAdd,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteDividerPreview(){
    FavoriteDivider(
        modifier = Modifier.padding(MaterialTheme.dimens.smallMargin),
        isShownNotes = true
    )
}