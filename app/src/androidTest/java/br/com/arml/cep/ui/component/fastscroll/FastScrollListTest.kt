package br.com.arml.cep.ui.component.fastscroll

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
    private lateinit var fastScrollListFab: String
    private lateinit var fastScrollListFabToStart: String
    private lateinit var fastScrollListFabToEnd: String

    private val items = List(100){ "Item $it" }
    private val shortedList = List(20){ "Item $it" }
    private val startItem = items.first()
    private val endItem = items.last()

    @Before
    fun setup(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            fastScrollListTag = getString(R.string.fastScrollList_component_testTag)
            fastScrollListFab = getString(R.string.fastScrollList_fab_testTag)
            fastScrollListFabToStart = getString(R.string.fastScrollList_fabToStart_contentDescription)
            fastScrollListFabToEnd = getString(R.string.fastScrollList_fabToEnd_contentDescription)
        }
    }

    fun fastScrollListContent(list: List<String> = items) {
        composeTestRule.setContent {
            FastScrollList(
                modifier = Modifier.testTag(fastScrollListTag)
            ){ lazyListState ->
                LazyColumn(state = lazyListState) {
                    items(list){ item -> Text(item) }
                }
            }
        }
    }

    @Test
    fun fastScrollList_shouldNotShowsFab_whenAllItemIsOnScreen(){
        fastScrollListContent(shortedList)
        composeTestRule.apply {
            onNodeWithTag(fastScrollListTag).assertExists()
            onNodeWithTag(fastScrollListFab).assertIsNotDisplayed()
            shortedList.forEach { onNodeWithText(it).assertIsDisplayed() }
        }
    }

    @Test
    fun fastScrollList_shouldShowsFab_whenNotAllItemIsOnScreen(){
        fastScrollListContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollListTag).assertExists()
            onNodeWithTag(fastScrollListFab).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowLastItem_whenFabIsClicked(){
        fastScrollListContent()
        composeTestRule.apply{
            onNodeWithContentDescription(fastScrollListFabToEnd)
                .assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNodeWithText(endItem).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowFirstItem_whenScreenShowsLastItemAndFabIsClicked(){
        fastScrollListContent()
        composeTestRule.apply {
            onNodeWithContentDescription(fastScrollListFabToEnd).performClick()
            waitForIdle()
            onNodeWithContentDescription(fastScrollListFabToStart)
                .assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNodeWithText(startItem).assertIsDisplayed()
        }
    }
}