package br.com.arml.cep.ui.screen.component.log

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.entity.toFormattedUTC
import java.sql.Timestamp

@Composable
fun LogElement(
    modifier: Modifier = Modifier,
    logEntry: LogEntry,
    onClickToDelete: (LogEntry) -> Unit,
    onClickToDetail: (LogEntry) -> Unit
){
    Surface(modifier = modifier.clickable { onClickToDetail(logEntry) }) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            LogInfo(
                modifier = Modifier.weight(1f),
                logEntry = logEntry
            )
            IconButton(
                onClick = { onClickToDelete(logEntry) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun LogInfo(
    modifier: Modifier = Modifier,
    logEntry: LogEntry
){
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.log_cep_field, Cep.format(logEntry.cep.text)),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(
                R.string.log_timestamp_field,
                logEntry.timestamp.toFormattedUTC()
            ),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogElementPreview(){
    LogElement(
        logEntry = LogEntry(
            cep = Cep.build("99999999"),
            timestamp = Timestamp(System.currentTimeMillis())
        ),
        onClickToDelete = {},
        onClickToDetail = {}
    )
}