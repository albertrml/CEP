package br.com.arml.cep.ui.screen.component.log.listpane.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogListHeader(
    modifier: Modifier = Modifier,
    date: String
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
        Text(
            modifier = Modifier,
            style = MaterialTheme.typography.labelLarge,
            text = date
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
    }
}