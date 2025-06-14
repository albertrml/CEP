package br.com.arml.cep.ui.screen.component.place.favorite

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header

@Composable
fun FavoritePlaceHeaderList(
    modifier: Modifier = Modifier
){
    Header(
        modifier = modifier,
        logo = Icons.Filled.Favorite,
        title = stringResource(R.string.favorite_title),
        colorLogo = Color.Red
    )
}

@Composable
fun FavoritePlaceHeaderDetails(
    modifier: Modifier = Modifier,
    onNavigateBackToList: () -> Unit
){
    Header(
        modifier = modifier,
        logo = Icons.AutoMirrored.Filled.ArrowBack,
        title = stringResource(R.string.favorite_details_title),
        onClickLogo = onNavigateBackToList
    )
}