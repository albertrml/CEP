package br.com.arml.cep.ui.component.common.field

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.field.SearchCepField
import br.com.arml.cep.utils.hasEditableText
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CepFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var query: String = ""
    private lateinit var searchCepFieldTag: String
    private lateinit var trailingIconDescription: String

    @Before
    fun setup() {
        query = ""
        composeTestRule.apply {
            InstrumentationRegistry.getInstrumentation().targetContext.apply {
                searchCepFieldTag = getString(R.string.searchCepField_component_testTag)
                trailingIconDescription = getString(R.string.searchCepField_clearTrailingIcon)
            }
            setContent { SearchCepField(onQueryChange = { query = it }) }
        }
    }

    private fun performInputAndCheck(input: String, expectedResult: String) {
        val node = composeTestRule.onNodeWithTag(searchCepFieldTag)

        node.performClick()
            .performTextInput(input)

        composeTestRule.waitForIdle()

        // Use the custom matcher to check only the editable text property
        node.assert(hasEditableText(expectedResult))

        // The onQueryChange callback is still important to verify
        Truth.assertThat(query).isEqualTo(expectedResult)

        node.performTextClearance()
    }

    @Test
    fun cepField_shouldFormatInput_whenTyping() {
        performInputAndCheck(input = "1", expectedResult = "1")
        performInputAndCheck(input = "12", expectedResult = "12")
        performInputAndCheck(input = "123", expectedResult = "123")
        performInputAndCheck(input = "1234", expectedResult = "1234")
        performInputAndCheck(input = "12345", expectedResult = "12345-")
        performInputAndCheck(input = "123456", expectedResult = "12345-6")
        performInputAndCheck(input = "1234567", expectedResult = "12345-67")
        performInputAndCheck(input = "12345678", expectedResult = "12345-678")
    }

    @Test
    fun cepField_shouldHandleNonDigitCharacters() {
        performInputAndCheck(input = "a1b2c3d4e5", expectedResult = "12345-")
    }

    @Test
    fun cepField_shouldNotExceedMaxLength() {
        performInputAndCheck(input = "1234567890123", expectedResult = "12345-678")
    }

    @Test
    fun cepField_shouldClearText_whenTrailingIconIsClicked() {
        val input = "12345"
        val expectedInput = "12345-"
        composeTestRule.apply{
            val node = onNodeWithTag(searchCepFieldTag)

            node.performTextInput(input)
            waitForIdle()
            node.assert(hasEditableText(expectedInput))

            onNodeWithContentDescription(trailingIconDescription).performClick()
            waitForIdle()
            node.assert(hasEditableText(""))
            Truth.assertThat(query).isEmpty()
        }
    }
}