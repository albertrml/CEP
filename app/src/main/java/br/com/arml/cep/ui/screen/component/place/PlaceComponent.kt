package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption

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
                PlaceFilterOption.None,
                PlaceFilterOption.ByCep
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
fun UnwantedPlaceComponentPreview(){
    UnwantedOptionComponent(
        onFilterByCep = {},
        onClickToShowAlert = {},
        onNoneFilter = {},
    )
}