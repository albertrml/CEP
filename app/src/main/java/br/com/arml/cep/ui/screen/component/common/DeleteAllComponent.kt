package br.com.arml.cep.ui.screen.component.common

import androidx.annotation.StringRes
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun DeleteAllComponent(
    modifier: Modifier = Modifier,
    @StringRes deleteLogAlertTitleId: Int,
    @StringRes deleteLogAlertTextId: Int,
    onConfirmDeleteAllEntries: () -> Unit = {}
){

    var showDeleteAlert by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
            .testTag(stringResource(R.string.deleteAllComponent_component_testTag)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .testTag(stringResource(R.string.deleteAllComponent_horizontalDivider_testTag)),
            thickness = MaterialTheme.dimens.mediumThickness
        )
        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.smallPadding))
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.deleteAllComponent_button_testTag)),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            enabled = !showDeleteAlert,
            onClick = { showDeleteAlert = true }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(
                    R.string.deleteAllComponent_icon_description
                ),
            )
            Text(
                text = stringResource(R.string.deleteAllComponent_button_label),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }

    if (showDeleteAlert) {
        CepAlertDialog(
            dialogTitle = stringResource(deleteLogAlertTitleId),
            dialogText = stringResource(deleteLogAlertTextId),
            onDismissRequest = { showDeleteAlert = false },
            onConfirmationRequest = {
                onConfirmDeleteAllEntries()
                showDeleteAlert = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteAllComponentPreview(){
    DeleteAllComponent(
        deleteLogAlertTitleId = R.string.log_delete_all_log_title,
        deleteLogAlertTextId = R.string.log_delete_all_log_alert,
        onConfirmDeleteAllEntries = {}
    )
}