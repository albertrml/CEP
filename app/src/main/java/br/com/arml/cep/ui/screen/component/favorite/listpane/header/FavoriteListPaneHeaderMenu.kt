package br.com.arml.cep.ui.screen.component.favorite.listpane.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.R.string.favoriteListPaneHeaderMenu_exportButton_description
import br.com.arml.cep.R.string.favoriteListPaneHeaderMenu_exportButton_testTag
import br.com.arml.cep.R.string.favoriteListPaneHeaderMenu_importButton_description
import br.com.arml.cep.R.string.favoriteListPaneHeaderMenu_importButton_testTag
import br.com.arml.cep.ui.theme.dimens

@Composable
fun FavoriteListPaneHeaderMenu(
    modifier: Modifier = Modifier,
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .testTag(
                stringResource(R.string.favoriteListPaneHeaderMenu_component_testTag)
            ),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier
                .testTag(
                    stringResource(favoriteListPaneHeaderMenu_exportButton_testTag)
                ),
            onClick = onExportClick
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(R.drawable.ic_export),
                contentDescription = stringResource(
                    favoriteListPaneHeaderMenu_exportButton_description
                ),
            )
        }

        Spacer(Modifier.padding(horizontal = MaterialTheme.dimens.smallSpacing))

        IconButton(
            modifier = Modifier
                .testTag(
                    stringResource(favoriteListPaneHeaderMenu_importButton_testTag)
                ),
            onClick = onImportClick
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(R.drawable.ic_import),
                contentDescription = stringResource(
                    favoriteListPaneHeaderMenu_importButton_description
                )
            )
        }
    }
}