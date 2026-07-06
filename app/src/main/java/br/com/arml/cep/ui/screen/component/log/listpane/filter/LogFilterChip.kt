package br.com.arml.cep.ui.screen.component.log.listpane.filter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.LogFilterOption

@Composable
fun LogFilterChip(
    modifier: Modifier = Modifier,
    labelFilter: LogFilterOption,
    isSelected: Boolean,
    onSelected: (LogFilterOption) -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = { onSelected(labelFilter) },
        label = { Text(labelFilter.name) },
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(
                        R.string.logFilterChip_filterSelected_description,
                        labelFilter.name
                    )
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LogFilterChipPreview() {
    LogFilterChip(
        modifier = Modifier.padding(MaterialTheme.dimens.smallPadding),
        labelFilter = LogFilterOption.ByCep,
        isSelected = true,
        onSelected = {}
    )
}