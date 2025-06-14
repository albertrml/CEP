package br.com.arml.cep.ui.screen.component.place.favorite

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.ui.screen.component.common.CepFilter
import br.com.arml.cep.ui.screen.component.common.FieldFilter
import br.com.arml.cep.ui.screen.component.place.PlaceFilterChips
import br.com.arml.cep.ui.utils.FavoriteFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions
import br.com.arml.cep.ui.utils.filterEnterTransition
import br.com.arml.cep.ui.utils.filterExitTransition

@Composable
fun FavoritePlaceFilter(
    modifier: Modifier = Modifier,
    onFilterByCep: (String) -> Unit = {},
    onFilterByTitle: (String) -> Unit = {},
    onNoneFilter: () -> Unit = {}
) {
    var selectedFilter by rememberSaveable(stateSaver = FavoriteFilterOption.saver) {
        mutableStateOf<FavoriteFilterOption>(FavoriteFilterOption.None)
    }
    val filters = favoriteFilterOptions
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlaceFilterChips(
            filters = filters,
            selectedFilter = selectedFilter,
            onSelectedFilter = { selectedFilter = it }
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