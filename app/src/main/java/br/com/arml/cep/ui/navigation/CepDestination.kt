package br.com.arml.cep.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.arml.cep.R

sealed class CepDestination(
    @field:StringRes val label: Int,
    val icon: ImageVector,
    @field:StringRes val contentDescription: Int
) {
    data object SearchDestination : CepDestination(
        label = R.string.navigation_searchDestination_label,
        icon = Icons.Default.Search,
        contentDescription = R.string.navigation_searchDestination_description
    )
    data object HistoryDestination : CepDestination(
        label = R.string.navigation_logDestination_label,
        icon = Icons.Default.History,
        contentDescription = R.string.navigation_logDestination_description
    )
    data object FavoriteDestination : CepDestination(
        label = R.string.navigation_favoriteDestination_label,
        icon = Icons.Default.Favorite,
        contentDescription = R.string.navigation_favoriteDestination_description
    )
    data object CacheDestination : CepDestination(
        label = R.string.navigation_cacheDestination_label,
        icon = Icons.Default.Storage,
        contentDescription = R.string.navigation_cacheDestination_description
    )

    companion object {
        private const val SEARCH_DEST_ID = "search"
        private const val HISTORY_DEST_ID = "log"
        private const val FAVORITE_DEST_ID = "favorite"
        private const val CACHE_DEST_ID = "cache"

        val Saver: Saver<CepDestination, String> = Saver(
            save = { destination ->
                when (destination) {
                    SearchDestination -> SEARCH_DEST_ID
                    HistoryDestination -> HISTORY_DEST_ID
                    FavoriteDestination -> FAVORITE_DEST_ID
                    CacheDestination -> CACHE_DEST_ID
                }
            },
            restore = { savedValue ->
                when (savedValue) {
                    SEARCH_DEST_ID -> SearchDestination
                    HISTORY_DEST_ID -> HistoryDestination
                    FAVORITE_DEST_ID -> FavoriteDestination
                    CACHE_DEST_ID -> CacheDestination
                    else -> SearchDestination
                }
            }
        )
    }

}

val cepDestinations = listOf(
    CepDestination.SearchDestination,
    CepDestination.HistoryDestination,
    CepDestination.FavoriteDestination,
    CepDestination.CacheDestination
)

@Composable
fun CepDestination.SelectDestination(
    onSearch: @Composable () -> Unit,
    onHistory: @Composable () -> Unit,
    onFavorite: @Composable () -> Unit,
    onCache: @Composable () -> Unit
){
    when(this){
        CepDestination.SearchDestination -> onSearch()
        CepDestination.HistoryDestination -> onHistory()
        CepDestination.FavoriteDestination -> onFavorite()
        CepDestination.CacheDestination -> onCache()
    }
}