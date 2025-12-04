package br.com.arml.cep.ui.screen.component.favorite.listpane

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.header.Header

@Composable
fun FavoriteDetailsHeader(
    modifier: Modifier = Modifier,
    onNavigateBackToList: () -> Unit
) {
    Header(
        modifier = modifier,
        logo = Icons.AutoMirrored.Filled.ArrowBack,
        title = stringResource(R.string.favorite_details_title),
        onClickLogo = onNavigateBackToList
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteHeaderDetailsPreview() {
    FavoriteDetailsHeader(
        onNavigateBackToList = {}
    )
}