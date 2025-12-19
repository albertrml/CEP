package br.com.arml.cep.ui.component.cache.listpane.item

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.cache.listpane.item.CachePlaceElement
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class CachePlaceElementTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val expectedPlace = mockUnfavoritePlaces.first()
    private val cachePlaceElementTag = ctx.getString(
        R.string.cachePlaceElement_component_testTag,
        expectedPlace.toString()
    )
    private val cachePlaceElementZipCodeText = ctx.getString(
        R.string.place_zipcode_text,
        expectedPlace.address.zipCode
    )
    private val cachePlaceElementDeleteIconTag = ctx.getString(
        R.string.cachePlaceElement_deleteIcon_testTag,
        expectedPlace.address.zipCode
    )

    @Test
    fun cachePlaceElement_shouldDisplayCepAndDeleteIcon(){
        composeTestRule.apply {
            setContent {
                CachePlaceElement(
                    place = expectedPlace,
                    onDeletePlace = {},
                    onNavigateToDetail = {}
                )
            }
            onNodeWithTag(cachePlaceElementTag).assertIsDisplayed()
            onNodeWithText(cachePlaceElementZipCodeText).assertIsDisplayed()
            onNodeWithTag(cachePlaceElementDeleteIconTag).assertIsDisplayed()
        }
    }

    @Test
    fun cachePlaceElement_shouldInvokesOnDeletePlaceCallback_whenDeleteIconIsClicked(){
        val mockOnDeletePlace: (Place) -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                CachePlaceElement(
                    place = expectedPlace,
                    onDeletePlace = mockOnDeletePlace,
                    onNavigateToDetail = {}
                )
            }
            onNodeWithTag(cachePlaceElementDeleteIconTag).performClick()
            verify { mockOnDeletePlace(expectedPlace) }
        }
    }

    @Test
    fun cachePlaceElement_shouldInvokesOnNavigateToDetailCallback_whenCachePlaceElementIsClicked(){
        val mockOnNavigateToDetailPane: (Place) -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                CachePlaceElement(
                    place = expectedPlace,
                    onDeletePlace = {},
                    onNavigateToDetail = mockOnNavigateToDetailPane
                )
            }
            onNodeWithTag(cachePlaceElementTag).performClick()
            verify { mockOnNavigateToDetailPane(expectedPlace) }
        }
    }
}