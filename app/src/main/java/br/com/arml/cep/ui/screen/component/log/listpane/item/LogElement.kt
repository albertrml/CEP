package br.com.arml.cep.ui.screen.component.log.listpane.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.utils.toFormattedUTC
import br.com.arml.cep.ui.theme.dimens
import java.sql.Timestamp

@Composable
fun LogElement(
    modifier: Modifier = Modifier,
    log: Log,
    onClickToDelete: (Log) -> Unit,
    onClickToDetail: (Log) -> Unit
) {
    Surface(
        modifier = modifier
            .clickable { onClickToDetail(log) }
            .testTag(
                stringResource(
                    R.string.logElement_component_testTag,
                    log.toString()
                )
            ),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(
            width = MaterialTheme.dimens.smallThickness,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.dimens.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LogElementContent(
                modifier = Modifier.weight(1f),
                log = log
            )
            IconButton(
                modifier = Modifier
                    .testTag(stringResource(R.string.logElement_deleteButton_testTag)),
                onClick = { onClickToDelete(log) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(
                        R.string.logElement_deleteButton_description,
                        log.cep.text, log.timestamp.toFormattedUTC()
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogElementPreview() {
    LogElement(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        log = Log(
            cep = Cep.build("99999-999"),
            timestamp = Timestamp(System.currentTimeMillis())
        ),
        onClickToDelete = {},
        onClickToDetail = {}
    )
}