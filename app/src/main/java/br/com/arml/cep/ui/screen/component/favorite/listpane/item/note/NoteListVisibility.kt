package br.com.arml.cep.ui.screen.component.favorite.listpane.item.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun NoteListVisibility(
    modifier: Modifier = Modifier,
    onChangeShownNotes: () -> Unit,
    isShownNotes: Boolean
) {
    Row(
        modifier = modifier
            .clickable { onChangeShownNotes() }
            .testTag(stringResource(R.string.noteListVisibility_component_testTag)),
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
                stringResource(id = R.string.noteListVisibility_hideText)
            else
                stringResource(id = R.string.noteListVisibility_showText),
        )
        Icon(
            imageVector = if (isShownNotes) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
            contentDescription = if (isShownNotes)
                stringResource(id = R.string.noteListVisibility_hideIcon_description)
            else
                stringResource(id = R.string.noteListVisibility_showIcon_description),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListVisibilityPreview(){
    var isShownNotes by remember { mutableStateOf(false) }
    NoteListVisibility(
        isShownNotes = isShownNotes,
        onChangeShownNotes = { isShownNotes = !isShownNotes }
    )
}