package br.com.arml.cep.ui.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.place.PlaceFilterComponent
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaceFilterChipTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var placeFilterChipComponent: String
    private lateinit var placeFilterChipComposable: String

    private val placeFilterByCepChip: String = PlaceFilterOption.ByCep.name
    private val placeFilterByTitleChip: String = PlaceFilterOption.ByTitle.name

    private lateinit var cepFilterTextField: String
    private lateinit var cepFilterSearchButton: String
    private lateinit var titleFilterTextField: String
    private lateinit var titleFilterSearchButton: String


    @Before
    fun setUp(){
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext

        placeFilterChipComponent = ctx.getString(R.string.testTag_placeFilter_component)
        placeFilterChipComposable = ctx.getString(R.string.testTag_placeFilter_composable)

        cepFilterTextField = ctx.getString(R.string.testTag_cepFilter_searchField)
        cepFilterSearchButton = ctx.getString(R.string.testTag_cepFilter_searchButton)

        titleFilterTextField = ctx.getString(R.string.testTag_titleFilter_field)
        titleFilterSearchButton = ctx.getString(R.string.testTag_titleFilter_button)

        displayPlaceFilterComponent()
    }

    fun displayPlaceFilterComponent(){
        composeTestRule.setContent {
            PlaceFilterComponent(
                modifier = Modifier.testTag(placeFilterChipComponent),
                filters = favoriteFilterOptions
            )
        }
    }

    @Test
    fun shouldDisplayAllOptions_whenPlaceFilterOptionsIsScrolled(){
        composeTestRule.apply {
            favoriteFilterOptions.forEach {
                onNodeWithTag(placeFilterChipComponent)
                    .performScrollToNode(hasText(it.name))
                    .assertExists()
            }
        }
    }

    @Test
    fun shouldDisplayCepFilterComponents_whenCepFilterOptionIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(placeFilterChipComponent)
                .performScrollToNode(matcher = hasText(placeFilterByCepChip))
            onNodeWithText(placeFilterByCepChip).performClick()
            waitForIdle()
            onNodeWithTag(cepFilterTextField).assertExists()
            onNodeWithTag(cepFilterSearchButton).assertExists()
        }
    }

    @Test
    fun shouldDisplayTitleFilterComponents_whenTitleFilterOptionIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(placeFilterChipComponent)
                .performScrollToNode(matcher = hasText(placeFilterByTitleChip))
            onNodeWithText(placeFilterByTitleChip).performClick()
            waitForIdle()
            onNodeWithTag(titleFilterTextField).assertExists()
            onNodeWithTag(titleFilterSearchButton).assertExists()
        }
    }
}