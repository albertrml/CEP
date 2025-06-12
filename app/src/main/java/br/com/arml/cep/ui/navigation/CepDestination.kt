package br.com.arml.cep.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.arml.cep.R

sealed class CepDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    data object SearchDestination : CepDestination(
        label = R.string.destination_search,
        icon = Icons.Default.Search,
        contentDescription = R.string.destination_search_description
    )
    data object HistoryDestination : CepDestination(
        label = R.string.destination_history,
        icon = Icons.AutoMirrored.Filled.List,
        contentDescription = R.string.destination_history_description
    )
    data object FavoriteDestination : CepDestination(
        label = R.string.destination_favorite,
        icon = Icons.Filled.Favorite,
        contentDescription = R.string.destination_favorite_description
    )
    data object CacheDestination : CepDestination(
        label = R.string.destination_cache,
        icon = Icons.Filled.FavoriteBorder,
        contentDescription = R.string.destination_favorite_description
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