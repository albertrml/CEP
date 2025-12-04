package br.com.arml.cep.ui.screen.component.common.filter.chip

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
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption

@Composable
fun PlaceFilterChip(
    modifier: Modifier = Modifier,
    labelFilter: PlaceFilterOption,
    isSelected: Boolean,
    //selectedLabelFilter: PlaceFilterOption,
    onSelected: (PlaceFilterOption) -> Unit
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
                        R.string.log_selected_filter_description,
                        labelFilter.name
                    ),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        end = MaterialTheme.dimens.smallSpacing
                    )
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SelectedPlaceFilterPreview(){
    PlaceFilterChip(
        labelFilter = PlaceFilterOption.ByCep,
        isSelected = true,
        //selectedLabelFilter = PlaceFilterOption.ByCep,
        onSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
fun UnselectedPlaceFilterPreview(){
    PlaceFilterChip(
        labelFilter = PlaceFilterOption.ByCep,
        //selectedLabelFilter = PlaceFilterOption.None,
        isSelected = false,
        onSelected = {}
    )
}