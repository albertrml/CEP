package br.com.arml.cep.ui.screen.component.place

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun ShowDeleteAlert(
    modifier: Modifier = Modifier,
    typeName: String,
    showDeleteAlert: () -> Unit,
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = MaterialTheme.dimens.mediumThickness
        )
        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.smallPadding))
        Button(
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            onClick = { showDeleteAlert() }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.show_delete_alert_description, typeName),
            )
            Text(
                text = stringResource(R.string.show_delete_alert_button),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }

}