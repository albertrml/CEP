package br.com.arml.cep.ui.screen.component.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.component.FavoriteDetailsHeader
import br.com.arml.cep.ui.screen.component.search.AddressForms
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteDetailsComponent(
    modifier: Modifier = Modifier,
    favorite: Pair<Address, Note?>,
    onNavigateBackToList: () -> Unit,
    onEditNote: (Note) -> Unit,
    onCreateNote: (Cep, Note) -> Unit,
) {
    val tabs = listOf(FavoriteTab.Notes, FavoriteTab.Address)
    val (address, note) = favorite
    val cep = Cep.build(address.zipCode.filter { it.isDigit() })

    var selectedTab by rememberSaveable(
        stateSaver = FavoriteTabSaver
    ) { mutableStateOf(FavoriteTab.Notes) }

    val selectedTabIndex by remember {
        derivedStateOf { tabs.indexOf(selectedTab) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            FavoriteDetailsHeader(
                modifier = Modifier
                    .testTag(stringResource(R.string.testTag_favoriteDetails_header)),
                onNavigateBackToList = onNavigateBackToList
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = tab == selectedTab,
                        onClick = { selectedTab = tabs[index] },
                        text = {
                            Text(
                                text = stringResource(tab.label),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }
            }

            NoteFromFavoriteFragment(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = MaterialTheme.dimens.smallPadding),
                selectedTab = selectedTab,
                address = address,
                note = note,
                onClick = { newNote ->
                    note?.let{ onEditNote(newNote) } ?: onCreateNote(cep,newNote)
                }
            )
        }
    }
}

@Composable
fun NoteFromFavoriteFragment(
    modifier: Modifier = Modifier,
    selectedTab: FavoriteTab,
    address: Address,
    note: Note?,
    onClick: (Note) -> Unit,
){
    when (selectedTab) {
        FavoriteTab.Address -> {
            AddressForms(
                modifier = modifier
                    .padding(horizontal = MaterialTheme.dimens.smallPadding),
                address = address
            )
        }

        FavoriteTab.Notes -> {
            FavoriteNotesComponent(
                modifier = modifier,
                note = note,
                onClick = { note -> onClick(note) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceDetailsPreview() {
    FavoriteDetailsComponent(
        modifier = Modifier,
        favorite = mockAddress(1) to mockNotes[0],
        onNavigateBackToList = {},
        onEditNote = {},
        onCreateNote = { _, _ -> }
    )
}