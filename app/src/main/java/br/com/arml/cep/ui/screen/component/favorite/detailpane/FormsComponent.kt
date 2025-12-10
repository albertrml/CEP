package br.com.arml.cep.ui.screen.component.favorite.detailpane

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.detailpane.note.NoteForms
import br.com.arml.cep.ui.screen.component.common.address.AddressForms
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FormsComponent(
    modifier: Modifier = Modifier,
    selectedTab: FavoriteTab,
    address: Address,
    note: Note?,
    onClick: (Note) -> Unit,
){
    when (selectedTab) {
        FavoriteTab.Address -> {
            AddressForms(
                modifier = modifier,
                address = address
            )
        }

        FavoriteTab.Notes -> {
            NoteForms(
                modifier = modifier,
                note = note,
                onClick = { note -> onClick(note) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressFormsComponentPreview(){
    val (address, note) = mockFavoritePlaces
        .first().let { it.address to it.notes.first() }
    FormsComponent(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        selectedTab = FavoriteTab.Address,
        address = address,
        note = note,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NoteFormsComponentPreview(){
    val (address, note) = mockFavoritePlaces.first().let { it.address to it.notes.first() }
    FormsComponent(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        selectedTab = FavoriteTab.Notes,
        address = address,
        note = note,
        onClick = {}
    )
}