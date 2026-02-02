package br.com.arml.cep.ui.component.common.filter

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.filter.CepFilter
import br.com.arml.cep.utils.hasEditableText
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CepFilterTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var cepFilterComposable: String
    private lateinit var cepFilterField: String
    private lateinit var cepFilterButton: String

    private val mockOnCepFilter: (String) -> Unit = mockk(relaxed = true)

    @Before
    fun setup(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            cepFilterComposable = getString(R.string.cepFilter_component_testTag)
            cepFilterField = getString(R.string.cepSearchField_component_testTag)
            cepFilterButton = getString(R.string.cepFilter_filterButton_testTag)
        }

        every { mockOnCepFilter(any()) } answers { println("mockOnCepFilter ${args[0]}") }

        composeTestRule.setContent { CepFilter(onFilterByCep = mockOnCepFilter) }
    }

    @Test
    fun cepFilter_shouldDisplayFieldAndButton(){
        composeTestRule.apply {
            onNodeWithTag(cepFilterComposable).assertExists()
            onNodeWithTag(cepFilterField).assertExists()
            onNodeWithTag(cepFilterButton).assertExists()
        }
    }

    @Test
    fun shouldDisplayCepAsHint_whenCepFieldIsBlank(){
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).assertExists()
            onNodeWithTag(cepFilterField).assert(hasText("CEP"))
        }
    }

    @Test
    fun shouldContainsOnlyDigits_whenUserInputsTextContainingAnyKindCharacters(){
        val query = "a1@2;3*4/5a6w7Q8"
        val expectedQuery = "12345-678"
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).apply {
                assertExists()
                performTextClearance()
                performTextInput(query)
                assertTextContains(expectedQuery)
            }
        }
    }

    @Test
    fun shouldUnableButton_whenUserInputsTextLesserThanMinLength(){
        val query = "a1@2"
        val expectedQuery = "12"
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).apply {
                performTextClearance()
                performTextInput(query)
                assert(hasEditableText(expectedQuery))
            }
            onNodeWithTag(cepFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldOnlyAcceptsEightDigits_whenUserInputsTextGreaterThanMaxLength(){
        val input = "1234567890123"
        val expectedInput = "12345-678"
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).apply {
                performTextClearance()
                performTextInput(input)
                assert(hasEditableText(expectedInput))
            }
            onNodeWithTag(cepFilterButton).assertIsEnabled()
        }
    }

    @Test
    fun shouldFilter_whenInputsValidQuery(){
        val query = "12345678"
        val expectedQuery = "12345-678"
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).performTextInput(query)
            onNodeWithTag(cepFilterButton).performClick()
            verify { mockOnCepFilter(expectedQuery) }
        }
    }
}