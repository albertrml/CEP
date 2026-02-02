package br.com.arml.cep.ui.screen.component.log.listpane.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.utils.toFormattedUTC
import br.com.arml.cep.ui.theme.dimens

@Composable
fun LogElementContent(
    modifier: Modifier = Modifier,
    log: Log
) {
    Column(
        modifier = modifier
            .testTag(
                stringResource(
                    R.string.logElementContent_component_testTag, log.toString()
                )
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = stringResource(
                    R.string.logElementContent_cepField_description,
                    log.cep.text
                )
            )
            Text(
                text = stringResource(R.string.logElementContent_cepField_label, log.cep.text),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = stringResource(
                    R.string.logElementContent_timestampField_description,
                    log.timestamp.toFormattedUTC()
                )
            )
            Text(
                text = log.timestamp.toFormattedUTC(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}