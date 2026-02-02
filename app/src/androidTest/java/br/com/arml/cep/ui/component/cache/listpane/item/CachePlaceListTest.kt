package br.com.arml.cep.ui.component.cache.listpane.item

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.cache.listpane.item.CachePlaceList
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CachePlaceListTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val scrollButtonTag = ctx.getString(R.string.fastScrollGrid_downButton_testTag)

    @Test
    fun cachePlaceList_shouldDisplayAllPlaces(){
        composeTestRule.apply{
            setContent {
                CachePlaceList(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onNavigateToDetail = {}
                )
            }
            mockUnfavoritePlaces.forEach { place ->
                val placeElementTag = ctx.getString(
                    R.string.cachePlaceElement_component_testTag,
                    place.toString()
                )

                val placeNode = onNodeWithTag(placeElementTag)
                if (placeNode.isNotDisplayed())
                    onNodeWithTag(scrollButtonTag).performClick()
                placeNode.assertIsDisplayed()
            }
        }
    }
    
    @Test
    fun cachePlaceList_shouldInvokesOnDeletePlaceCallback_whenDeleteButtonIsClicked(){
        val mockOnDeletePlace: (Place) -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                CachePlaceList(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = { place -> mockOnDeletePlace(place) },
                    onNavigateToDetail = {}
                )
            }
            mockUnfavoritePlaces.forEach { place ->
                val deleteButtonTag = ctx.getString(
                    R.string.cachePlaceElement_deleteIcon_testTag,
                    place.address.zipCode
                )

                val deleteButtonNode = onNodeWithTag(deleteButtonTag)
                if (deleteButtonNode.isNotDisplayed())
                    onNodeWithTag(scrollButtonTag).performClick()
                deleteButtonNode.performClick()
                verify (exactly = 1) { mockOnDeletePlace(place) }
            }
        }
    }

    @Test
    fun cachePlaceList_shouldInvokesOnNavigateToDetailCallback_whenCachePlaceElementIsClicked(){
        val mockOnNavigateToDetail: (Place) -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                CachePlaceList(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onNavigateToDetail = { place -> mockOnNavigateToDetail(place) }
                )
            }
            mockUnfavoritePlaces.forEach { place ->
                val placeElementTag = ctx.getString(
                    R.string.cachePlaceElement_component_testTag,
                    place.toString()
                )

                val placeNode = onNodeWithTag(placeElementTag)
                if (placeNode.isNotDisplayed())
                    onNodeWithTag(scrollButtonTag).performClick()
                placeNode.performClick()
                verify (exactly = 1) { mockOnNavigateToDetail(place) }
            }
        }
    }
}