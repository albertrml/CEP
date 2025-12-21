package br.com.arml.cep.ui.screen.component.cache.listpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.R.string.cacheListPaneOnSuccess_cachePlaceList_testTag
import br.com.arml.cep.R.string.cacheListPaneOnSuccess_deleteAllComponent_testTag
import br.com.arml.cep.R.string.cacheListPaneOnSuccess_placeFilterComponent_testTag
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.cache.listpane.item.CachePlaceList
import br.com.arml.cep.ui.screen.component.common.DeleteAllComponent
import br.com.arml.cep.ui.screen.component.common.filter.chip.PlaceFilterComponent
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.cacheFilterOptions

@Composable
fun CacheListPaneOnSuccess(
    modifier: Modifier = Modifier,
    places: List<Place>,
    selectedFilter: PlaceFilterOption,
    onSelectedFilter: (PlaceFilterOption) -> Unit,
    onCepFilter: (String) -> Unit,
    onClearFilter: () -> Unit,
    onDeletePlace: (Place) -> Unit,
    onDeleteAllCache: () -> Unit,
    onNavigateToDetail: (Place) -> Unit,
){
    Column(
        modifier = modifier
            .testTag(stringResource(R.string.cacheListPaneOnSuccess_component_testTag)),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlaceFilterComponent(
            modifier = Modifier
                .testTag(stringResource(cacheListPaneOnSuccess_placeFilterComponent_testTag)),
            filters = cacheFilterOptions,
            selectedFilter = selectedFilter,
            onSelectedFilter = { selectedFilter -> onSelectedFilter(selectedFilter) },
            onFilterByCep = { query -> onCepFilter(query) },
            onNoneFilter = { onClearFilter() }
        )
        DeleteAllComponent(
            modifier = Modifier
                .testTag(stringResource(cacheListPaneOnSuccess_deleteAllComponent_testTag)),
            deleteLogAlertTitleId = R.string.cachePlaceAlert_title_text,
            deleteLogAlertTextId = R.string.cachePlaceAlert_content_text,
            onConfirmDeleteAllEntries = { onDeleteAllCache() }
        )
        CachePlaceList(
            modifier = Modifier
                .testTag(stringResource(cacheListPaneOnSuccess_cachePlaceList_testTag)),
            places = places,
            onDeletePlace = { place -> onDeletePlace(place) },
            onNavigateToDetail = { place -> onNavigateToDetail(place) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CacheListPaneOnSuccessPreview(){
    var selectedFilter by rememberSaveable(stateSaver = PlaceFilterOption.saver) {
        mutableStateOf(PlaceFilterOption.None)
    }
    CacheListPaneOnSuccess(
        places = mockUnfavoritePlaces,
        selectedFilter = selectedFilter,
        onSelectedFilter = { selectedFilter = it },
        onCepFilter = {},
        onClearFilter = {},
        onDeletePlace = {},
        onDeleteAllCache = {},
        onNavigateToDetail = {},
    )
}