package br.com.arml.cep.ui.screen.component.favorite.listpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.common.filter.chip.PlaceFilterComponent
import br.com.arml.cep.ui.screen.component.favorite.FavoriteListPaneComponent
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions

@Composable
fun FavoriteListPaneComponentOnSuccess(
    modifier: Modifier = Modifier,
    places: List<Place>,
    selectedFilter: PlaceFilterOption,
    onSelectedFilter: (PlaceFilterOption) -> Unit,
    onCepFilter: (String) -> Unit,
    onTitleFilter: (String) -> Unit,
    onNoneFilter: () -> Unit,
    onAddNote: (Cep, Note) -> Unit,
    onFavoriteIconClick: (Place) -> Unit,
    onDeleteNote: (Pair<Cep, Note>) -> Unit,
    onNavigateToDetails: (Pair<Address, Note>) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.mediumPadding)
    ) {
        PlaceFilterComponent(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(
                    R.string.favoriteListPaneComponentOnSuccess_placeFilterComponent_testTag)
                ),
            filters = favoriteFilterOptions,
            selectedFilter = selectedFilter,
            onSelectedFilter = { selectedFilter -> onSelectedFilter(selectedFilter) },
            onFilterByCep = { query -> onCepFilter(query) },
            onFilterByTitle = { query -> onTitleFilter(query) },
            onNoneFilter = { onNoneFilter() }
        )

        FavoriteListComponent(
            modifier = Modifier
                .testTag(stringResource(
                    R.string.favoriteListPaneComponentOnSuccess_favoriteListComponent_testTag)
                ),
            places = places,
            onAddNote = onAddNote,
            onFavoriteIconClick = onFavoriteIconClick,
            onDeleteNote = onDeleteNote,
            onNavigateToDetail = onNavigateToDetails
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteDetailPaneComponentOnSuccessPreview() {
    val response = Response.Success(mockFavoritePlaces)
    FavoriteListPaneComponent(
        state = FavoriteState(places = response),
        onCepFilter = {},
        onTitleFilter = {},
        onNoneFilter = {},
        onAddNote = { _, _ -> },
        onFavoriteIconClick = {},
        onDeleteNote = {},
        onNavigateToDetails = {},
    )
}