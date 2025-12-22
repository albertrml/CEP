package br.com.arml.cep.ui.component.common.field

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.field.CepTextField
import br.com.arml.cep.utils.hasEditableText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var appTextFieldTest: String
    private lateinit var appTextFieldInputCounter: String
    private val fieldName = "Título"
    private val errorMessage = "Não pode ter menos que 3 caracteres"
    private val maxSize = 5

    @Before
    fun setup() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            appTextFieldTest = getString(R.string.cepTextField_component_testTag)
            appTextFieldInputCounter = getString(R.string.cepTextField_inputCounter_testTag)
        }
    }

    private fun setTextFieldContent(
        text: MutableState<String> = mutableStateOf(""),
        nameField: String = fieldName,
        isError: Boolean = false,
        errorMessage: String = "",
        maxSize: Int = this.maxSize,
        showInputSize: Boolean = false,
    ) {
        composeTestRule.setContent {
            CepTextField(
                nameField = nameField,
                text = text.value,
                onChangeText = { text.value = it },
                isError = isError,
                errorMessage = errorMessage,
                maxSize = maxSize,
                showInputSize = showInputSize,
            )
        }
    }

    // ---------------------------
    // Tests
    // ---------------------------

    @Test
    fun appTextField_shouldDisplayLabel() {
        setTextFieldContent()
        composeTestRule.onNodeWithText(fieldName).assertIsDisplayed()
    }

    @Test
    fun appTextField_shouldUpdateTextAndRespectMaxSize() {
        val textState = mutableStateOf("")

        setTextFieldContent(text = textState)

        val input = "abcdef"
        val expected = "abcde"

        composeTestRule.onNodeWithTag(appTextFieldTest)
            .performTextInput(input)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(appTextFieldTest)
            .assert(hasEditableText(expected))

        assert(textState.value == expected)
    }

    @Test
    fun appTextField_shouldShowErrorMessage_whenIsErrorIsTrue() {
        setTextFieldContent(
            isError = true,
            errorMessage = errorMessage
        )

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun appTextField_shouldShowAndCorrectlyUpdateInputCounter() {
        val textState = mutableStateOf("")
        val query = "123"

        setTextFieldContent(
            text = textState,
            showInputSize = true
        )

        // Estado inicial
        composeTestRule.onNodeWithText("0/$maxSize").assertIsDisplayed()

        // Digitando
        composeTestRule.onNodeWithTag(appTextFieldTest)
            .performTextInput(query)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(appTextFieldInputCounter)
            .assertTextEquals("${query.length}/$maxSize")
    }
}