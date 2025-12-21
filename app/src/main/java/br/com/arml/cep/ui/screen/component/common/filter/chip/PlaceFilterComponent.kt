package br.com.arml.cep.ui.screen.component.common.filter.chip

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.ui.screen.component.common.filter.CepFilter
import br.com.arml.cep.ui.screen.component.common.filter.TitleFilter
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions
import br.com.arml.cep.ui.utils.filterEnterTransition
import br.com.arml.cep.ui.utils.filterExitTransition

@Composable
fun PlaceFilterComponent(
    modifier: Modifier = Modifier,
    filters: List<PlaceFilterOption>,
    onFilterByCep: (String) -> Unit = {},
    onFilterByTitle: (String) -> Unit = {},
    onNoneFilter: () -> Unit = {}
) {
    var selectedFilter by rememberSaveable(stateSaver = PlaceFilterOption.saver) {
        mutableStateOf(PlaceFilterOption.None)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlaceFilterList(
            filters = filters,
            selectedFilter = selectedFilter,
            onSelectedFilter = {
                selectedFilter = it
                if (it == PlaceFilterOption.None) {
                    onNoneFilter()
                }
            }
        )
        AnimatedContent(
            targetState = selectedFilter,
            transitionSpec = {
                filterEnterTransition
                    .togetherWith(filterExitTransition) using SizeTransform(clip = true)
            }
        ) { targetFilter ->
            when (targetFilter) {
                PlaceFilterOption.ByCep -> {
                    CepFilter(
                        onFilterByCep = { cep ->
                            keyboardController?.hide()
                            onFilterByCep(cep)
                        }
                    )
                }

                PlaceFilterOption.ByTitle -> {
                    TitleFilter(
                        maxSize = MAX_TITLE_LENGTH,
                        onFilterByTitle = { title ->
                            keyboardController?.hide()
                            onFilterByTitle(title)
                        }
                    )
                }

                PlaceFilterOption.None -> {
                    keyboardController?.hide()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceFilterComponentPreview() {
    PlaceFilterComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.smallMargin),
        filters = favoriteFilterOptions,
        onFilterByCep = {  },
        onFilterByTitle = {  },
        onNoneFilter = {  }
    )
}