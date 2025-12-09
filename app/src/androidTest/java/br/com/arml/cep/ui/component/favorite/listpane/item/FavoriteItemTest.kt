package br.com.arml.cep.ui.component.favorite.listpane.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.FavoriteItem
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class FavoriteItemTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    
    private val favoriteItemComponent = ctx.getString(R.string.favoriteItem_component_testTag)
    private val favoriteItemActionBar = ctx.getString(R.string.favoriteItemActionBar_component_testTag)
    private val noteListComponent = ctx.getString(R.string.noteListComponent_component_testTag)

    @Test
    fun favoriteItem_shouldDisplayFavoriteItemActionBarAndNoteListComponent(){
        composeTestRule.apply{
            setContent {
                FavoriteItem(
                    place = mockFavoritePlaces.first(),
                    favoriteIcon = Icons.Default.Favorite,
                    colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
                    onFavoriteIconClick = {},
                    onAddNote = { _, _ -> },
                    onDeleteNote = {},
                    onNavigateToDetail = {}
                )
            }
            onNodeWithTag(favoriteItemComponent).assertExists()
            onNodeWithTag(favoriteItemActionBar).assertExists()
            onNodeWithTag(noteListComponent).assertExists()
        }
    }
}