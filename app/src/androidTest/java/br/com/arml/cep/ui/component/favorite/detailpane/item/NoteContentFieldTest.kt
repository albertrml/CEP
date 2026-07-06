package br.com.arml.cep.ui.component.favorite.detailpane.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_CONTENT_LENGTH
import br.com.arml.cep.ui.screen.component.favorite.detailpane.note.NoteContentField
import br.com.arml.cep.utils.hasEditableText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteContentFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val noteContentFieldComponent = ctx.getString(
        R.string.noteContentField_component_testTag
    )
    private val noteContentFieldLabel = ctx.getString(
        R.string.noteContentField_nameField_text
    )

    @Test
    fun noteContentField_shouldDisplaysLabelContentAndSizeInput_whenValidContentIsInserted() {
        val validContent = LoremIpsum(100)
            .values
            .joinToString(" ")
            .take(MAX_CONTENT_LENGTH)

        val expectedInputSize = "${validContent.length}/$MAX_CONTENT_LENGTH"

        composeTestRule.apply {
            setContent {
                NoteContentField(
                    content = validContent,
                    onContentChange = {}
                )
            }

            onNodeWithTag(noteContentFieldComponent)
                .assertExists()
                .assertTextContains(noteContentFieldLabel)
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
        val expectedInput = invalidInput.take(MAX_CONTENT_LENGTH)
        val expectedInputSize = "$MAX_CONTENT_LENGTH/$MAX_CONTENT_LENGTH"

        var input by mutableStateOf("")
        val onInputChange: (String) -> Unit = { input = it }
        composeTestRule.apply {
            setContent {
                NoteContentField(
                    content = input,
                    onContentChange = onInputChange
                )
            }

            onNodeWithTag(noteContentFieldComponent)
                .performTextInput(invalidInput)

            onNodeWithTag(noteContentFieldComponent)
                .assert(hasEditableText(expectedInput))

            onNodeWithText(expectedInputSize)
                .assertExists()
        }
    }
}