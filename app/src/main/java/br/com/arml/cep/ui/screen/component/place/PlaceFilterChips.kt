package br.com.arml.cep.ui.screen.component.place

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.ui.screen.component.common.CepFilter
import br.com.arml.cep.ui.screen.component.common.FieldFilter
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.FavoriteFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions
import br.com.arml.cep.ui.utils.filterEnterTransition
import br.com.arml.cep.ui.utils.filterExitTransition

@Composable
fun PlaceFilterComponent(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit,
    onFilterByTitle: (String) -> Unit,
    onNoneFilter: () -> Unit,
    selectedFilter: FavoriteFilterOption,
    onChangeFilter: (FavoriteFilterOption) -> Unit
) {
    val filters = favoriteFilterOptions
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlaceFilterChips(
            filters = filters,
            selectedFilter = selectedFilter,
            onSelectedFilter = { onChangeFilter(it) }
        )
        AnimatedContent(
            targetState = selectedFilter,
            transitionSpec = {
                filterEnterTransition
                    .togetherWith(filterExitTransition) using SizeTransform(clip = true)
            }
        ) { targetFilter ->
            when (targetFilter) {
                FavoriteFilterOption.ByCep -> {
                    CepFilter(
                        onFilterByCep = {
                            keyboardController?.hide()
                            onFilterByCep(it)
                        }
                    )
                }
                FavoriteFilterOption.ByTitle -> {
                    FieldFilter(
                        nameFilter = stringResource(R.string.favorite_title_field_filter),
                        maxSize = MAX_TITLE_LENGTH,
                        onFilterByCep = {
                            keyboardController?.hide()
                            onFilterByTitle(it)
                        }
                    )
                }
                FavoriteFilterOption.None -> {
                    keyboardController?.hide()
                    onNoneFilter()
                }
            }
        }
    }
}

@Composable
fun PlaceFilterChips(
    modifier: Modifier = Modifier,
    filters: List<FavoriteFilterOption>,
    selectedFilter: FavoriteFilterOption,
    onSelectedFilter: (FavoriteFilterOption) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(filters) { topic ->
            PlaceFilterChip(
                labelFilter = topic,
                isSelected = topic === selectedFilter,
                onSelected = { onSelectedFilter(it) }
            )
        }
    }
}

@Composable
fun PlaceFilterChip(
    modifier: Modifier = Modifier,
    labelFilter: FavoriteFilterOption,
    isSelected: Boolean,
    onSelected: (FavoriteFilterOption) -> Unit
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
            } else {
                null
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PlaceFilterPreview() {
    var filter by rememberSaveable(stateSaver = FavoriteFilterOption.saver) {
        mutableStateOf<FavoriteFilterOption>(FavoriteFilterOption.None)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PlaceFilterComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.largeMargin * 2)
                .padding(horizontal = MaterialTheme.dimens.mediumMargin),
            selectedFilter = filter,
            onFilterByCep = {},
            onFilterByTitle = {},
            onNoneFilter = {},
            onChangeFilter = { filter = it }
        )
    }
}