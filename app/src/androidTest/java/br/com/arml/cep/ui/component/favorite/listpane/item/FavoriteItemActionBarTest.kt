package br.com.arml.cep.ui.component.favorite.listpane.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.FavoriteItemActionBar
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteItemActionBarTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry
        .getInstrumentation().targetContext

    private val favoriteItemActionBarTag = ctx
        .getString(R.string.favoriteItemActionBar_component_testTag)

    @Test
    fun favoriteItemActionBar_shouldDisplayTitleTextAndFavoriteIconButton(){
        val place = mockFavoritePlaces.first()
        val favoriteItemActionBarIconButtonTag = ctx.getString(
            R.string.favoriteItemActionBar_favoriteIcon_testTag,
            place.cep.text
        )
        val expectedTitle = ctx.getString(
            R.string.favoriteItemActionBar_titleText_zipcode,
            place.cep.text
        )

        composeTestRule.apply {
            setContent {
                FavoriteItemActionBar(
                    place = place,
                    favoriteIcon = Icons.Default.Favorite,
                    colorFavoriteIcon = MaterialTheme.colorScheme.onSurface
                )
            }

            onNodeWithTag(favoriteItemActionBarTag).assertExists()
            onNodeWithTag(favoriteItemActionBarIconButtonTag).assertExists()
            onNodeWithText(expectedTitle).assertExists()
        }
    }

    @Test
    fun favoriteItemActionBar_shouldInvokesOnFavoriteChangesCallback_whenFavoriteIconIsClicked(){
        val place = mockFavoritePlaces.first()
        val onFavoriteChanges: (Place) -> Unit = mockk(relaxed = true)
        val favoriteItemActionBarIconButtonTag = ctx.getString(
            R.string.favoriteItemActionBar_favoriteIcon_testTag,
            place.cep.text
        )

        composeTestRule.apply {
            setContent {
                FavoriteItemActionBar(
                    place = place,
                    favoriteIcon = Icons.Default.Favorite,
                    colorFavoriteIcon = MaterialTheme.colorScheme.onSurface,
                    onFavoriteChanges = onFavoriteChanges
                )
            }

            onNodeWithTag(favoriteItemActionBarIconButtonTag).performClick()
            verify(exactly = 1) { onFavoriteChanges(place) }
        }
    }
}