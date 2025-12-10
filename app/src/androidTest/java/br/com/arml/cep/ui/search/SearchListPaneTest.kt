package br.com.arml.cep.ui.search

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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

    /*** Header ***/
    private lateinit var searchListPaneHeader: String
    private lateinit var searchListPaneIconHeader: String
    private lateinit var searchListPaneTitleHeader: String

    /*** CEP Field and Search Button ***/
    private lateinit var searchListPaneCepField: String
    private lateinit var searchListPaneSearchButton: String
    private val mockOnSearchCep: (String) -> Unit = mockk()

    /*** Queries ***/
    private lateinit var validQuery: String
    private lateinit var validQueryWithNonDigit: String
    private lateinit var invalidQuery: String

    @Before
    fun setUp(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.apply {
            searchListPaneHeader = getString(R.string.testTag_searchScreen_listPane_header)
            searchListPaneIconHeader = getString(R.string.headerContent_iconButton_testTag)
            searchListPaneTitleHeader = getString(R.string.header_titleText)
            searchListPaneCepField = getString(R.string.testTag_searchScreen_listPane_cepField)
            searchListPaneSearchButton = getString(R.string.testTag_searchScreen_listPane_searchButton)
        }
        validQuery = "12345678"
        validQueryWithNonDigit = "a1@2;3*4/5a6w7Q8"
        invalidQuery = "a1@2;3*4/5a6w7Q"
        every { mockOnSearchCep(any()) } answers {
            println("mockOnSearchCep CALLED")
        }
    }

    private fun setDisplayScreenContent() {
        composeTestRule.setContent { SearchListPane(onSearchCep = mockOnSearchCep) }
    }

    /*** Header ***/
    @Test
    fun shouldShowTitleAndIconOnHeader_whenSearchScreenListPaneIsCalled(){
        setDisplayScreenContent()
        composeTestRule.apply {
            onNodeWithTag(searchListPaneHeader).assertExists()
            onNodeWithTag(searchListPaneTitleHeader).assertExists()
            onNodeWithTag(searchListPaneIconHeader).assertExists()
        }
    }

    /*** CEP Field and Search Button ***/
    @Test
    fun shouldDisplayCEPAsHint_whenCEPFieldIsBlank(){
        setDisplayScreenContent()
        composeTestRule.apply {
            onNodeWithTag(searchListPaneCepField).assertExists()
            onNodeWithTag(searchListPaneCepField).assert(hasText("CEP"))
        }
    }

    @Test
    fun shouldPerformSearch_whenInputInCepFieldIsValid(){
        setDisplayScreenContent()
        composeTestRule.onNodeWithTag(searchListPaneCepField).apply{
            assertExists()
            performTextInput(validQuery)
            assert(hasText(Cep.build(validQuery).text))
            performClick()
        }
    }

    @Test
    fun shouldPerformSearch_whenFilteredInputIsValid_inCepField(){
        val filteredQuery = validQueryWithNonDigit.filter { it.isDigit() }
        setDisplayScreenContent()
        composeTestRule.onNodeWithTag(searchListPaneCepField).apply{
            assertExists()
            performTextInput(validQueryWithNonDigit)
            assert(
                matcher = hasText(
                    text = Cep.build(filteredQuery).text)
            )
        }
        composeTestRule.onNodeWithTag(searchListPaneSearchButton).apply{
            assertExists()
            assertIsEnabled()
            performClick()
        }
        verify { mockOnSearchCep(filteredQuery) }
    }

    @Test
    fun shouldNotPerformSearch_whenFilteredInputIsInvalid_inCepField(){
        setDisplayScreenContent()
        composeTestRule.onNodeWithTag(searchListPaneCepField).apply {
            assertExists()
            performTextInput(invalidQuery)
        }
        composeTestRule.onNodeWithTag(searchListPaneSearchButton).apply {
            assertExists()
            assertIsNotEnabled()
        }
    }
}