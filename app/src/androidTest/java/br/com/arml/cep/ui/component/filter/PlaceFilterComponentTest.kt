package br.com.arml.cep.ui.component.filter

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
import br.com.arml.cep.ui.screen.component.common.filter.chip.PlaceFilterComponent
import br.com.arml.cep.ui.utils.PlaceFilterOption
import br.com.arml.cep.ui.utils.favoriteFilterOptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaceFilterComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var placeFilterChipComponent: String
    private lateinit var placeFilterChipComposable: String

    private val placeFilterByCepChip: String = PlaceFilterOption.ByCep.name
    private val placeFilterByTitleChip: String = PlaceFilterOption.ByTitle.name

    private lateinit var searchCepField: String
    private lateinit var cepFilterSearchButton: String
    private lateinit var titleFilterTextField: String
    private lateinit var titleFilterSearchButton: String


    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply{
            placeFilterChipComponent = getString(R.string.testTag_placeFilter_component)
            placeFilterChipComposable = getString(R.string.testTag_placeFilter_composable)

            searchCepField = getString(R.string.searchCepField_component_testTag)
            cepFilterSearchButton = getString(R.string.cepFilter_filterButton)

            titleFilterTextField = getString(R.string.titleFilter_searchField_testTag)
            titleFilterSearchButton = getString(R.string.titleFilter_filterButton_testTag)
        }


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
            onNodeWithTag(searchCepField).assertExists()
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