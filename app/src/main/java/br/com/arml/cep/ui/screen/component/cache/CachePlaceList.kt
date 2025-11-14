package br.com.arml.cep.ui.screen.component.cache

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.common.ScrollableFab
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CachePlaceList(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onDeleteIconClick: (Place) -> Unit,
    onFavoriteIconClick: (Place) -> Unit,
    onNavigateToDetail: (Place) -> Unit
){
    val lazyListState = rememberLazyListState()
    ScrollableFab(listState = lazyListState){
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ){
            items(places){ place ->
                CachePlaceElement(
                    modifier = Modifier.clickable(onClick = { onNavigateToDetail(place) }),
                    place = place,
                    onNavigateToDetail = onNavigateToDetail,
                    onFavoriteIconClick = onFavoriteIconClick,
                    onDeleteIconClick = onDeleteIconClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CachePlaceListPreview(){
    CachePlaceList(
        places = mockUnfavoritePlaces,
        onDeleteIconClick = {},
        onFavoriteIconClick = {},
        onNavigateToDetail = {},
    )
}