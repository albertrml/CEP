package br.com.arml.cep.ui.screen.component.favorite.listpane.header

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.header.Header

@Composable
fun FavoriteListPaneHeader(
    modifier: Modifier = Modifier,
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {}
) {
    Header(
        modifier = modifier
            .testTag(stringResource(R.string.favoriteListPaneHeader_component_testTag)),
        logo = Icons.Filled.Favorite,
        title = stringResource(R.string.favoriteListPaneHeader_title_label),
        menu = {
            FavoriteListPaneHeaderMenu(
                onImportClick = onImportClick,
                onExportClick = onExportClick
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteHeaderListPanePreview() {
    FavoriteListPaneHeader()
}