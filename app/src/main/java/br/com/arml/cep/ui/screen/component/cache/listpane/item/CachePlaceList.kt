package br.com.arml.cep.ui.screen.component.cache.listpane.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollGrid
import br.com.arml.cep.ui.theme.dimens

@Composable
fun CachePlaceList(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onDeletePlace: (Place) -> Unit,
    onNavigateToDetail: (Place) -> Unit
){
    FastScrollGrid(
        staggeredGridState = rememberLazyStaggeredGridState()
    ){ staggeredGridState ->
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            state = staggeredGridState,
            columns = StaggeredGridCells.Adaptive(minSize = MaterialTheme.dimens.minSize),
            verticalItemSpacing = MaterialTheme.dimens.mediumSpacing,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ){
            items(places){ place ->
                CachePlaceElement(
                    place = place,
                    onDeletePlace = onDeletePlace,
                    onNavigateToDetail = onNavigateToDetail
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
        onDeletePlace = {},
        onNavigateToDetail = {},
    )
}