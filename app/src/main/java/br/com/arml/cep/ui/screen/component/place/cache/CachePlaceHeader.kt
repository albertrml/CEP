package br.com.arml.cep.ui.screen.component.place.cache

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header

@Composable
fun CachePlaceHeaderList(
    modifier: Modifier = Modifier
){
    Header(
        modifier = modifier,
        logo = Icons.Default.FavoriteBorder,
        title = stringResource(R.string.cache_title)
    )
}