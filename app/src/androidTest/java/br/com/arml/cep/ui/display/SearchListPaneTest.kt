package br.com.arml.cep.ui.display

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.ui.screen.component.search.SearchListPane
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchListPaneTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var searchListPaneHeader: String
    private lateinit var searchListPaneIconHeader: String
    private lateinit var searchListPaneTitleHeader: String
    private lateinit var searchListPaneCepField: String
    private lateinit var searchListPaneSearchButton: String
    private val mockOnSearchCep: (String) -> Unit = mockk()
    private lateinit var validQuery: String
    private lateinit var invalidQuery: String

    @Before
    fun setUp(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.apply {
            searchListPaneHeader = getString(R.string.testTag_searchScreen_listPane_header)
            searchListPaneIconHeader = getString(R.string.testTag_header_icon)
            searchListPaneTitleHeader = getString(R.string.testTag_header_title)
            searchListPaneCepField = getString(R.string.testTag_searchScreen_listPane_cepField)
            searchListPaneSearchButton = getString(R.string.testTag_searchScreen_listPane_searchButton)
        }
        validQuery = "12345678"
        invalidQuery = "a1@2;3*4/5a6w7Q8"
        every { mockOnSearchCep(any()) } answers {
            println("mockOnSearchCep CALLED")
        }
    }

    private fun setDisplayScreenContent() {
        composeTestRule.setContent { SearchListPane(onSearchCep = mockOnSearchCep) }
    }

    @Test
    fun searchListPane_shouldShowTitleAndIconOnHeader(){
        setDisplayScreenContent()
        composeTestRule.apply {
            onNodeWithTag(searchListPaneHeader).assertExists()
            onNodeWithTag(searchListPaneTitleHeader).assertExists()
            onNodeWithTag(searchListPaneIconHeader).assertExists()
        }
    }

    @Test
    fun searchListPane_shouldShowValidQueryOnCepFieldAndPerformSearch(){
        setDisplayScreenContent()
        composeTestRule.apply {
            onNodeWithTag(searchListPaneCepField).assertExists()
            onNodeWithTag(searchListPaneCepField).performTextInput(validQuery)
            onNodeWithTag(searchListPaneCepField).assert(
                hasText(Cep.build(validQuery).toFormattedCep()))
            onNodeWithTag(searchListPaneSearchButton).assertExists()
            onNodeWithTag(searchListPaneSearchButton).performClick()
            verify { mockOnSearchCep(validQuery) }
        }
    }

    @Test
    fun searchListPane_shouldShowInvalidQueryButItIsFilteredOnCepFieldAndPerformSearch(){
        setDisplayScreenContent()
        composeTestRule.apply {
            onNodeWithTag(searchListPaneCepField).assertExists()
            onNodeWithTag(searchListPaneCepField).performTextInput(invalidQuery)
            onNodeWithTag(searchListPaneCepField).assert(
                hasText(Cep.build(validQuery).toFormattedCep()))
            onNodeWithTag(searchListPaneSearchButton).assertExists()
            onNodeWithTag(searchListPaneSearchButton).performClick()
            verify { mockOnSearchCep(validQuery) }
        }
    }
}