package br.com.arml.cep.ui.component.common.fastscroll

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FastScrollListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fastScrollListTag: String
    private lateinit var fastScrollListUpButtonTag: String
    private lateinit var fastScrollListDownButtonTag: String

    private val items = List(100) { "Item $it" }
    private val shortedList = List(20) { "Item $it" }
    private val startItem = items.first()
    private val endItem = items.last()

    @Before
    fun setup() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            fastScrollListTag = getString(R.string.fastScrollList_component_testTag)
            fastScrollListUpButtonTag = getString(R.string.fastScrollList_upButton_testTag)
            fastScrollListDownButtonTag = getString(R.string.fastScrollList_downButton_testTag)
        }
    }

    fun fastScrollListContent(list: List<String> = items) {
        composeTestRule.setContent {
            FastScrollList { items(list) { item -> Text(item) } }
        }
    }

    @Test
    fun fastScrollList_shouldNotShowsButtons_whenAllItemIsOnScreen() {
        fastScrollListContent(shortedList)
        composeTestRule.apply {
            onNodeWithTag(fastScrollListTag).assertExists()
            onNodeWithTag(fastScrollListDownButtonTag).assertIsNotDisplayed()
            onNodeWithTag(fastScrollListUpButtonTag).assertIsNotDisplayed()
            shortedList.forEach { onNodeWithText(it).assertIsDisplayed() }
        }
    }

    @Test
    fun fastScrollList_shouldShowsDownButton_whenAllItemIsNotOnScreen() {
        fastScrollListContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollListTag).assertExists()
            onNodeWithTag(fastScrollListDownButtonTag).assertIsDisplayed()
            onNodeWithTag(fastScrollListUpButtonTag).assertIsNotDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowsLastItemAndUpButton_whenListIsAtStartAndDownButtonIsClicked() {
        fastScrollListContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollListDownButtonTag).performClick()
            waitForIdle()
            onNodeWithText(endItem).assertIsDisplayed()
            onNodeWithTag(fastScrollListUpButtonTag).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowFirstItemAndDownButton_whenListIsAtTheEndAndUpButtonIsClicked() {
        fastScrollListContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollListDownButtonTag).performClick()
            waitForIdle()
            onNodeWithText(endItem).assertIsDisplayed()
            onNodeWithTag(fastScrollListUpButtonTag).performClick()
            waitForIdle()
            onNodeWithTag(fastScrollListDownButtonTag).assertIsDisplayed()
            onNodeWithText(startItem).assertIsDisplayed()
        }
    }
}