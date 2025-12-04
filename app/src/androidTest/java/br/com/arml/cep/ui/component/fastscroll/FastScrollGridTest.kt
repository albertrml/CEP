package br.com.arml.cep.ui.component.fastscroll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.fastscroll.FastScrollGrid
import br.com.arml.cep.ui.theme.dimens
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FastScrollGridTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var fastScrollGridTag: String
    private lateinit var fastScrollGridFab: String
    private lateinit var fastScrollGridFabToStart: String
    private lateinit var fastScrollGridFabToEnd: String

    private val items = List(100){ "Item $it" }
    private val startItem = items.first()
    private val endItem = items.last()
    private val shortedList = List(20){ "Item $it" }

    @Before
    fun setup(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            fastScrollGridTag = getString(R.string.fastScrollGrid_component_testTag)
            fastScrollGridFab = getString(R.string.fastScrollGrid_fab_testTag)
            fastScrollGridFabToStart = getString(R.string.fastScrollGrid_fabToStart_contentDescription)
            fastScrollGridFabToEnd = getString(R.string.fastScrollGrid_fabToEnd_contentDescription)
        }
    }

    fun fastScrollGridContent(list: List<String> = items){
        composeTestRule.setContent {
            FastScrollGrid(
                modifier = Modifier.testTag(fastScrollGridTag)
            ){ staggeredGridState ->
                LazyVerticalStaggeredGrid(
                    state = staggeredGridState,
                    columns = StaggeredGridCells.Adaptive(minSize = MaterialTheme.dimens.minSize),
                    verticalItemSpacing = MaterialTheme.dimens.mediumSpacing,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
                ){
                    items(list){ item ->
                        Text(
                            modifier = Modifier.width(180.dp),
                            text = item
                        )
                    }
                }
            }
        }
    }

    @Test
    fun fastScrollList_shouldNotShowsFab_whenAllItemIsOnScreen(){
        fastScrollGridContent(shortedList)
        composeTestRule.apply {
            onNodeWithTag(fastScrollGridTag).assertExists()
            onNodeWithTag(fastScrollGridFab).assertIsNotDisplayed()
            shortedList.forEach { onNodeWithText(it).assertIsDisplayed() }
        }
    }

    @Test
    fun fastScrollList_shouldShowsFab_whenNotAllItemIsOnScreen(){
        fastScrollGridContent()
        composeTestRule.apply {
            onNodeWithTag(fastScrollGridTag).assertExists()
            onNodeWithTag(fastScrollGridFab).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowLastItem_whenFabIsClicked(){
        fastScrollGridContent()
        composeTestRule.apply{
            onNodeWithContentDescription(fastScrollGridFabToEnd)
                .assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNodeWithText(endItem).assertIsDisplayed()
        }
    }

    @Test
    fun fastScrollList_shouldShowFirstItem_whenScreenShowsLastItemAndFabIsClicked(){
        fastScrollGridContent()
        composeTestRule.apply {
            onNodeWithContentDescription(fastScrollGridFabToEnd).performClick()
            waitForIdle()
            onNodeWithContentDescription(fastScrollGridFabToStart)
                .assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNodeWithText(startItem).assertIsDisplayed()
        }
    }
}