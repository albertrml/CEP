package br.com.arml.cep.ui.component.common.fastscroll

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollGrid
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FastScrollGridTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var fastScrollGridTag: String
    private lateinit var fastScrollGridDownButtonTag: String
    private lateinit var fastScrollGridUpButtonTag: String

    private val items = List(100) { "Item $it" }
    private val startItem = items.first()
    private val endItem = items.last()
    private val shortedList = List(20) { "Item $it" }

    @Before
    fun setup() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            fastScrollGridTag = getString(R.string.fastScrollGrid_component_testTag)
            fastScrollGridUpButtonTag = getString(R.string.fastScrollGrid_upButton_testTag)
            fastScrollGridDownButtonTag = getString(R.string.fastScrollGrid_downButton_testTag)
        }
    }

    fun fastScrollGridContent(list: List<String> = items) {
        composeTestRule.setContent {
            FastScrollGrid {
                items(list) { item ->
                    Text(
                        modifier = Modifier.width(180.dp),
                        text = item
                    )
                }
            }
        }
    }

    @Test
    fun fastScrollList_shouldNotShowsFab_whenAllItemIsOnScreen() {
        fastScrollGridContent(shortedList)
        composeTestRule.apply {
            onNodeWithTag(fastScrollGridTag).assertExists()
            onNodeWithTag(fastScrollGridUpButtonTag).assertIsNotDisplayed()
            onNodeWithTag(fastScrollGridDownButtonTag).assertIsNotDisplayed()
            shortedList.forEach { onNodeWithText(it).assertIsDisplayed() }
        }
    }

    @Test
    fun fastScrollList_shouldShowsDownButton_whenAllItemIsNotOnScreen() {
        fastScrollGridContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollGridTag).assertExists()
            onNodeWithTag(fastScrollGridUpButtonTag).assertIsNotDisplayed()
            onNodeWithTag(fastScrollGridDownButtonTag).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowLastItemAndUpButton_whenDownButtonIsClicked() {
        fastScrollGridContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollGridDownButtonTag).performClick()
            waitForIdle()
            onNodeWithTag(fastScrollGridUpButtonTag).assertIsDisplayed()
            onNodeWithTag(fastScrollGridDownButtonTag).assertIsNotDisplayed()
            onNodeWithText(endItem).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowFirstItemAndDownButton_whenListIsAtTheEndAndUpButtonIsClicked() {
        fastScrollGridContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollGridDownButtonTag).performClick()
            waitForIdle()
            onNodeWithText(endItem).assertIsDisplayed()
            onNodeWithTag(fastScrollGridUpButtonTag).assertIsDisplayed().performClick()
            waitForIdle()
            onNodeWithText(startItem).assertIsDisplayed()
            onNodeWithTag(fastScrollGridDownButtonTag).assertIsDisplayed()
        }
    }
}