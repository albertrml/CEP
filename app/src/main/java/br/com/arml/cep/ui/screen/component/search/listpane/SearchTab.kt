package br.com.arml.cep.ui.screen.component.search.listpane

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.arml.cep.R

sealed class SearchTab(
    @field:StringRes val label: Int,
    val icon: ImageVector,
    @field:StringRes val contentDescription: Int
){
    data object Address : SearchTab(
        label = R.string.searchTab_addressTab_label,
        icon = Icons.Outlined.Home,
        contentDescription = R.string.searchTab_addressTab_description
    )

    data object CEP : SearchTab(
        label = R.string.searchTab_cepTab_label,
        icon = Icons.Outlined.LocationOn,
        contentDescription = R.string.searchTab_cepTab_description
    )
}

val SearchTabSaver = Saver<SearchTab, String>(
    save = { it::class.java.simpleName },
    restore = {
        when (it) {
            "Address" -> SearchTab.Address
            "CEP" -> SearchTab.CEP
            else -> throw IllegalArgumentException("Unknown tab: $it")
        }
    }
)

val SearchTabValues = listOf(SearchTab.CEP, SearchTab.Address)