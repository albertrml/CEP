package br.com.arml.cep.ui.favorite

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockFavoritePlaceEntries
import br.com.arml.cep.ui.screen.component.favorite.FavoriteExtraComponent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteExtraPaneTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var favoriteExtraHeaderComponent: String
    private lateinit var favoriteExtraTitleHeader: String
    private lateinit var favoriteExtraBackButtonHeader: String
    private lateinit var favoriteTitleNoteField: String
    private lateinit var favoriteContentNoteField: String
    private lateinit var favoriteUpdateButton: String

    private val onClickToUpdateListener: (PlaceEntry) -> Unit = mockk(relaxed = true)
    private val onNavigateBackToDetailsListener: () -> Unit = mockk(relaxed = true)

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            favoriteExtraHeaderComponent = getString(R.string.testTag_favoriteExtraScreen_header_component)
            favoriteExtraTitleHeader = getString(R.string.testTag_header_title)
            favoriteExtraBackButtonHeader = getString(R.string.testTag_header_icon)
            favoriteTitleNoteField = getString(R.string.testTag_favoriteExtraScreen_titleNoteField)
            favoriteContentNoteField = getString(R.string.testTag_favoriteExtraScreen_contentNoteField)
            favoriteUpdateButton = getString(R.string.testTag_favoriteExtraScreen_updateButton)
        }
    }

    fun displayFavoriteExtra(placeEntry: PlaceEntry = mockFavoritePlaceEntries.first()){
        composeTestRule.setContent {
            FavoriteExtraComponent(
                modifier = Modifier.testTag(favoriteExtraHeaderComponent),
                placeEntry = placeEntry,
                onClickToUpdate = { onClickToUpdateListener(placeEntry) },
                onNavigateBackToDetails = onNavigateBackToDetailsListener
            )
        }
    }

    @Test
    fun shouldDisplayFavoriteExtraHeaderComponents_whenDisplayFavoriteExtraIsCalled(){
        displayFavoriteExtra()
        composeTestRule.apply{
            onNodeWithTag(favoriteExtraHeaderComponent).assertExists()
            onNodeWithTag(favoriteExtraTitleHeader).assertExists()
            onNodeWithTag(favoriteExtraBackButtonHeader).assertExists()
            onNodeWithTag(favoriteTitleNoteField).assertExists()
            onNodeWithTag(favoriteContentNoteField).assertExists()
            onNodeWithTag(favoriteUpdateButton).assertExists()
        }
    }

    @Test
    fun shouldUpdatePlaceEntry_whenUpdateButtonIsClicked(){
        val placeEntry = mockFavoritePlaceEntries.first()
        displayFavoriteExtra(placeEntry)
        composeTestRule.onNodeWithTag(favoriteUpdateButton).performClick()
        verify { onClickToUpdateListener(placeEntry) }
    }

    @Test
    fun shouldGoBack_whenBackButtonIsClicked(){
        displayFavoriteExtra()
        composeTestRule.onNodeWithTag(favoriteExtraBackButtonHeader).performClick()
        verify { onNavigateBackToDetailsListener() }
    }
}