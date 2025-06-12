package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.common.CepFilter
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.FavoriteFilterOption

@Composable
fun FavoritePlaceComponent(
    modifier: Modifier = Modifier,
    placeEntries: List<PlaceEntry>,
    selectedFilter: FavoriteFilterOption,
    onFilterByCep: (String) -> Unit,
    onFilterByTitle: (String) -> Unit,
    onFilterNone: () -> Unit,
    onClickToUnwanted: (PlaceEntry) -> Unit,
    onClickToEdit: (PlaceEntry) -> Unit,
    onChangeFilter: (FavoriteFilterOption) -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        Header(
            modifier = modifier,
            logo = Icons.Filled.Favorite,
            title = stringResource(R.string.favorite_title),
            colorLogo = Color.Red
        )
        PlaceFilterComponent(
            modifier = Modifier.fillMaxWidth(),
            selectedFilter = selectedFilter,
            onFilterByCep = { onFilterByCep(it) },
            onFilterByTitle = { onFilterByTitle(it) },
            onNoneFilter = { onFilterNone() },
            onChangeFilter = { onChangeFilter(it) }
        )
        PlaceList(
            placeEntries = placeEntries,
            logo = Icons.Filled.Favorite,
            colorLogo = Color.Red,
            onClickLogo = onClickToUnwanted,
            onClickEntry = onClickToEdit
        )
    }
}

@Composable
fun UnwantedPlaceComponent(
    modifier: Modifier = Modifier,
    placeEntries: List<PlaceEntry>,
    onFilterByCep: (String) -> Unit,
    onClickToFavorite: (PlaceEntry) -> Unit,
    onClickToDetails: (PlaceEntry) -> Unit,
    onClickToDelete: (PlaceEntry) -> Unit,
    onClickToShowAlert: (Boolean) -> Unit
){
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        Header(
            modifier = modifier,
            logo = Icons.Default.FavoriteBorder,
            title = stringResource(R.string.cache_title),
        )
        CepFilter(
            onFilterByCep = { onFilterByCep(it) }
        )

        ShowDeleteAlert(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.smallPadding),
            typeName = stringResource(R.string.show_delete_alert_cache),
            showDeleteAlert = { onClickToShowAlert(it) }
        )

        PlaceList(
            placeEntries = placeEntries,
            logo = Icons.Default.FavoriteBorder,
            colorLogo = MaterialTheme.colorScheme.onSurface,
            onClickToDelete = { onClickToDelete(it) },
            onClickLogo = onClickToFavorite,
            onClickEntry = onClickToDetails
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePlaceComponentPreview(){
    var filter by rememberSaveable(stateSaver = FavoriteFilterOption.saver) {
        mutableStateOf<FavoriteFilterOption>(FavoriteFilterOption.None)
    }

    FavoritePlaceComponent(
        placeEntries = mockPlaceEntries,
        onFilterByCep = {},
        selectedFilter = filter,
        onFilterByTitle = {},
        onFilterNone = {},
        onClickToUnwanted = {},
        onClickToEdit = {},
        onChangeFilter = { filter = it }
    )
}

@Preview(showBackground = true)
@Composable
fun UnwantedPlaceComponentPreview(){
    UnwantedPlaceComponent(
        placeEntries = mockPlaceEntries,
        onFilterByCep = {},
        onClickToFavorite = {},
        onClickToDetails = {},
        onClickToDelete = {},
        onClickToShowAlert = {},
    )
}