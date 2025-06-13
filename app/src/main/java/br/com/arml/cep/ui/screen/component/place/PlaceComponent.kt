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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.FavoriteFilterOption

@Composable
fun FavoriteOptionComponent(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit,
    onFilterByTitle: (String) -> Unit,
    onFilterNone: () -> Unit,
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
            onFilterByCep = { onFilterByCep(it) },
            onFilterByTitle = { onFilterByTitle(it) },
            onNoneFilter = { onFilterNone() },
        )
    }
}

@Composable
fun UnwantedOptionComponent(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit,
    onNoneFilter: () -> Unit,
    onClickToShowAlert: () -> Unit,
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
        PlaceFilterComponent(
            modifier = Modifier.fillMaxWidth(),
            filters = listOf(
                FavoriteFilterOption.None,
                FavoriteFilterOption.ByCep
            ),
            onFilterByCep = { onFilterByCep(it) },
            onNoneFilter = { onNoneFilter() },
        )

        ShowDeleteAlert(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.smallPadding),
            typeName = stringResource(R.string.show_delete_alert_cache),
            showDeleteAlert = { onClickToShowAlert() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePlaceComponentPreview(){
    FavoriteOptionComponent(
        onFilterByCep = {},
        onFilterByTitle = {},
        onFilterNone = {},
    )
}

@Preview(showBackground = true)
@Composable
fun UnwantedPlaceComponentPreview(){
    UnwantedOptionComponent(
        onFilterByCep = {},
        onClickToShowAlert = {},
        onNoneFilter = {},
    )
}