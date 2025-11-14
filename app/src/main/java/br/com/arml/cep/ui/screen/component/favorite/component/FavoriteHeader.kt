package br.com.arml.cep.ui.screen.component.favorite.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteListHeader(
    modifier: Modifier = Modifier,
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {}
) {
    Header(
        modifier = modifier,
        logo = Icons.Filled.Favorite,
        title = stringResource(R.string.favorite_title),
        menu = {
            FavoriteListHeaderMenu(
                onImportClick = onImportClick,
                onExportClick = onExportClick
            )
        }
    )
}

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

@Composable
fun FavoriteExtraHeader(
    modifier: Modifier = Modifier,
    onNavigateBackToList: () -> Unit
) {
    Header(
        modifier = modifier,
        logo = Icons.AutoMirrored.Filled.ArrowBack,
        title = stringResource(R.string.favorite_extra_title),
        onClickLogo = onNavigateBackToList
    )
}

@Composable
fun FavoriteListHeaderMenu(
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_favoriteListHeader_exportButton)),
            onClick = onExportClick
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(R.drawable.ic_export),
                contentDescription = "Export favorite",
            )
        }

        Spacer(Modifier.padding(horizontal = MaterialTheme.dimens.smallSpacing))

        IconButton(
            modifier = Modifier
                .testTag(stringResource(R.string.testTag_favoriteListHeader_importButton)),
            onClick = onImportClick
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(R.drawable.ic_import),
                contentDescription = "Import favorite"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteHeaderListPreview() {
    FavoriteListHeader()
}

@Preview(showBackground = true)
@Composable
fun FavoriteHeaderDetailsPreview() {
    FavoriteDetailsHeader(
        onNavigateBackToList = {}
    )
}