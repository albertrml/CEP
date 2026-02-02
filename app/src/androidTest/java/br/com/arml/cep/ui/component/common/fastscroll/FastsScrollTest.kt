package br.com.arml.cep.ui.component.common.fastscroll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScroll
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class FastsScrollTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var fastScrollTag: String
    private lateinit var fastScrollFab: String
    private lateinit var fastScrollFabToStart: String
    private lateinit var fastScrollFabToEnd: String
    private val items = List(100){ "Item $it" }
    private val startItem = items.first()
    private val endItem = items.last()


    @Before
    fun setup(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            fastScrollTag = getString(R.string.fastScroll_component_testTag)
            fastScrollFab = getString(R.string.fastScroll_fab_testTag)
            fastScrollFabToStart = getString(R.string.fastScroll_upFab_contentDescription)
            fastScrollFabToEnd = getString(R.string.fastScroll_downFab_contentDescription)
        }
    }

    fun fastScrollContent(list: List<String> = items){
        composeTestRule.setContent {
            FastScroll { scrollState ->
                Column(modifier = Modifier.verticalScroll(scrollState) ){
                    list.forEach { Text(text = it) }
                }
            }
        }
    }

    @Test
    fun fastScroll_shouldNotShowsFab_whenAllItemIsOnScreen(){
        val shortedList = List(20){ "Item $it" }
        fastScrollContent(shortedList)
        composeTestRule.apply {
            onNodeWithTag(fastScrollTag).assertExists()
            onNodeWithTag(fastScrollFab).assertIsNotDisplayed()
            shortedList.forEach {   onNodeWithText(it).assertIsDisplayed() }
        }
    }

    @Test
    fun fastScroll_shouldShowsFab_whenNotAllItemIsOnScreen(){
        fastScrollContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollTag).assertExists()
            onNodeWithTag(fastScrollFab).assertIsDisplayed()
        }
    }

    @Test
    fun fastScroll_shouldShowLastItem_whenFabIsClicked(){
        fastScrollContent()
        composeTestRule.apply{
            onNodeWithContentDescription(fastScrollFabToEnd)
                .assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNodeWithText(endItem).assertIsDisplayed()
        }
    }

    @Test
    fun fastScroll_shouldShowFirstItem_whenScreenShowsLastItemAndFabIsClicked(){
        fastScrollContent()
        composeTestRule.apply {
            onNodeWithContentDescription(fastScrollFabToEnd).performClick()
            waitForIdle()
            onNodeWithContentDescription(fastScrollFabToStart)
                .assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNodeWithText(startItem).assertIsDisplayed()
        }
    }
}