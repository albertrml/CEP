package br.com.arml.cep.ui.screen.component.favorite

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.arml.cep.R

sealed class FavoriteTab(
    @field:StringRes val label: Int,
    val icon: ImageVector,
    @field:StringRes val contentDescription: Int
) {
    data object Address : FavoriteTab(
        label = R.string.favorite_address_tab,
        icon = Icons.Outlined.LocationOn,
        contentDescription = R.string.favorite_address_tab_description
    )

    data object Notes : FavoriteTab(
        label = R.string.favorite_notes_tab,
        icon = Icons.Outlined.LocationOn,
        contentDescription = R.string.favorite_notes_tab_description
    )

}

val FavoriteTabSaver = Saver<FavoriteTab, String>(
    save = { it::class.java.simpleName },
    restore = {
        when (it) {
            "Address" -> FavoriteTab.Address
            "Notes" -> FavoriteTab.Notes
            else -> throw IllegalArgumentException("Unknown tab: $it")
        }
    }
)