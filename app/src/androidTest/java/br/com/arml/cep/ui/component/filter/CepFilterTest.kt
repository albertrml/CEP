package br.com.arml.cep.ui.component.filter

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
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.ui.screen.component.common.CepFilter
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

    /*** Queries ***/
    private lateinit var validQuery: String
    private lateinit var validQueryWithNonDigit: String
    private lateinit var validQueryWithNonDigitFiltered: String
    private lateinit var invalidQuery: String
    private lateinit var invalidQueryFiltered: String

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            cepFilterComposable = getString(R.string.testTag_cepFilter_composable)
            cepFilterField = getString(R.string.testTag_cepFilter_searchField)
            cepFilterButton = getString(R.string.testTag_cepFilter_searchButton)
        }

        every { mockOnCepFilter(any()) } answers { println("mockOnCepFilter ${args[0]}") }

        /*** Queries ***/
        validQuery = "12345678"
        validQueryWithNonDigit = "a1@2;3*4/5a6w7Q8"
        invalidQuery = "a1@2"
        validQueryWithNonDigitFiltered = validQueryWithNonDigit.filter { it.isDigit() }
        invalidQueryFiltered = invalidQuery.filter { it.isDigit() }

        composeTestRule.setContent { CepFilter(onFilterByCep = mockOnCepFilter) }
    }

    @Test
    fun shouldDisplayComposableFieldAndButton_whenCepFilterIsCalled(){
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
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).apply {
                assertExists()
                performTextClearance()
                performTextInput(validQueryWithNonDigit)
                assertTextContains(Cep
                    .build(validQueryWithNonDigitFiltered)
                    .toFormattedCep()
                )
            }
        }
    }

    @Test
    fun shouldUnableButton_whenUserInputsTextLesserThanMinLength(){
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).apply {
                performTextClearance()
                performTextInput(invalidQuery)
                assert(hasText("12"))
            }
            onNodeWithTag(cepFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldOnlyAcceptsEightDigits_whenUserInputsTextGreaterThanMaxLength(){
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).apply {
                performTextClearance()
                performTextInput(validQueryWithNonDigit+"91234")
                assert(hasText("12345-678"))
            }
            onNodeWithTag(cepFilterButton).assertIsEnabled()
        }
    }

    @Test
    fun shouldFilter_whenInputsValidQuery(){
        composeTestRule.apply {
            onNodeWithTag(cepFilterField).performTextInput(validQuery)
            onNodeWithTag(cepFilterButton).performClick()
            verify { mockOnCepFilter(validQuery) }
        }
    }
}