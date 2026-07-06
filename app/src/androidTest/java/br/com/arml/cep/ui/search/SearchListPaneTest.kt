package br.com.arml.cep.ui.search

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.search.SearchListPane
import br.com.arml.cep.ui.screen.component.search.listpane.SearchTab
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class SearchListPaneTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val headerTag = ctx.getString(R.string.header_component_testTag)
    private val searchFieldTag = ctx.getString(R.string.cepSearchField_component_testTag)
    private val searchButtonTag = ctx.getString(R.string.searchListPane_searchButton_testTag)

    private val mockOnSearchCep: (String) -> Unit = mockk(relaxed = true)

    @Test
    fun searchListPaneTest_shouldDisplayHeader_whenSearchListPaneIsCalled(){
        composeTestRule.apply {
            setContent {
                SearchListPane(
                    onSearchCep = mockOnSearchCep,
                    selectedTab = SearchTab.CEP
                )
            }
            onNodeWithTag(headerTag).assertExists()
            onNodeWithTag(searchFieldTag).assertExists()
            onNodeWithTag(searchButtonTag).assertExists()
        }
    }

    @Test
    fun searchListPaneTest_shouldUnableSearchButton_whenQueryIsInvalid(){
        val invalidQueries = listOf(
            "1234567",
            "a1s2d3f4g5h6"
        )
        composeTestRule.apply {
            setContent {
                SearchListPane(
                    onSearchCep = mockOnSearchCep,
                    selectedTab = SearchTab.CEP
                )
            }
            invalidQueries.forEach { query ->
                onNodeWithTag(searchFieldTag).performTextInput(query)
                onNodeWithTag(searchButtonTag).assertIsNotEnabled()
                onNodeWithTag(searchFieldTag).performTextClearance()
            }
        }
    }

    @Test
    fun searchListPaneTest_shouldInvokesOnSearchCepCallback_whenSearchButtonIsEnabledAndClicked(){
        val query = "12345678"
        val expectedQuery = "12345-678"
        composeTestRule.apply {
            setContent {
                SearchListPane(
                    onSearchCep = mockOnSearchCep,
                    selectedTab = SearchTab.CEP
                )
            }
            onNodeWithTag(searchFieldTag).performTextInput(query)
            onNodeWithTag(searchButtonTag).assertIsEnabled().performClick()
            verify(exactly = 1) { mockOnSearchCep(expectedQuery) }
        }
    }
}