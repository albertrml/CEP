package br.com.arml.cep.ui.screen.component.cep.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import br.com.arml.cep.ui.theme.dimens

@Composable
fun AddressField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isDividerVisible: Boolean = true,
    thickness: Dp = MaterialTheme.dimens.smallThickness
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (isDividerVisible) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = thickness,
            )
        }
    }
}