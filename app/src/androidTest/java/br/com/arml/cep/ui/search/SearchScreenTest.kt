package br.com.arml.cep.ui.search

import androidx.compose.ui.test.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.cep.search.SearchScreen
import br.com.arml.cep.ui.theme.CEPTheme
import io.mockk.every
import org.junit.Rule
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnSearchCep: (String) -> Unit = mockk()
    private lateinit var cepFieldLabelText: String
    private lateinit var buttonText: String
    private lateinit var titleText: String

    @Before
    fun setUp() {
        every { mockOnSearchCep(any()) } answers {
            println("mockOnSearchCep CALLED with: ${args[0]}")
        }
        composeTestRule.setContent {
            CEPTheme {
                SearchScreen(
                    modifier = Modifier.fillMaxSize(),
                    onSearchCep = mockOnSearchCep
                )

                cepFieldLabelText = stringResource(R.string.search_cep_field_name)
                buttonText = stringResource(R.string.search_button_label)
                titleText = stringResource(R.string.search_title)
            }
        }
    }

    @Test
    fun searchScreen_shouldCallsOnSearchCep_whenSearchButtonIsClicked(){
        val cepToSearch = "12345678"
        val cepFormatted = "12345-678"

        val textField = composeTestRule.onNodeWithText(cepFieldLabelText)
        val button = composeTestRule.onNodeWithText(buttonText)

        textField.performTextInput(cepToSearch)
        composeTestRule.onNodeWithText(cepFormatted).assertExists()

        button.performClick()
        verify { mockOnSearchCep(cepToSearch) }
    }

    @Test
    fun searchScreen_displaysHeaderCorrectly() {
        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
    }

}