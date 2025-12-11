package br.com.arml.cep.ui.component.favorite.detailpane.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.ui.screen.component.favorite.detailpane.note.NoteTitleField
import br.com.arml.cep.utils.hasEditableText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteTitleFieldTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val noteTitleFieldComponent = ctx.getString(
        R.string.noteTitleField_component_testTag
    )
    private val noteTitleFieldLabel = ctx.getString(
        R.string.noteTitleField_nameField_text
    )

    @Test
    fun noteContentField_shouldDisplaysLabelContentAndSizeInput_whenValidContentIsInserted() {
        val validContent = LoremIpsum(100)
            .values
            .joinToString(" ")
            .take(MAX_TITLE_LENGTH)

        val expectedInputSize = "${validContent.length}/$MAX_TITLE_LENGTH"

        composeTestRule.apply {
            setContent {
                NoteTitleField(
                    title = validContent,
                    onTitleChange = {}
                )
            }

            onNodeWithTag(noteTitleFieldComponent)
                .assertExists()
                .assertTextContains(noteTitleFieldLabel)
                .assert(hasEditableText(validContent))

            onNodeWithText(expectedInputSize)
                .assertExists()
                .assertTextContains(expectedInputSize)
        }
    }

    @Test
    fun noteContentField_shouldAcceptsMaxInputSize_whenInputSizeIsReached() {
        val invalidInput = LoremIpsum(100)
            .values
            .joinToString(" ")
        val expectedInput = invalidInput.take(MAX_TITLE_LENGTH)
        val expectedInputSize = "$MAX_TITLE_LENGTH/$MAX_TITLE_LENGTH"

        var input by mutableStateOf("")
        val onInputChange: (String) -> Unit = { input = it }
        composeTestRule.apply {
            setContent {
                NoteTitleField(
                    title = input,
                    onTitleChange = onInputChange
                )
            }

            onNodeWithTag(noteTitleFieldComponent)
                .performTextInput(invalidInput)

            onNodeWithTag(noteTitleFieldComponent)
                .assert(hasEditableText(expectedInput))

            onNodeWithText(expectedInputSize)
                .assertExists()
        }
    }
}