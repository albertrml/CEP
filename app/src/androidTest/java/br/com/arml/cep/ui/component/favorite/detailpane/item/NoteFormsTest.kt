package br.com.arml.cep.ui.component.favorite.detailpane.item

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.detailpane.note.NoteForms
import br.com.arml.cep.utils.hasEditableText
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteFormsTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val noteTitleFieldTag = ctx.getString(R.string.noteTitleField_component_testTag)
    private val noteContentFieldTag = ctx.getString(R.string.noteContentField_component_testTag)
    private val createButtonText = ctx.getString(R.string.noteFormsButton_saveButton_text)
    private val updateButtonText = ctx.getString(R.string.noteFormsButton_updateButton_text)

    private val mockOnClick: (Note) -> Unit = mockk(relaxed = true)

    @Test
    fun noteForms_shouldDisplayCorrectly_whenNoteIsProvided() {
        val note = mockNotes.first()
        composeTestRule.apply {
            setContent {
                NoteForms(note = note, onClick = mockOnClick)
            }

            onNodeWithText(note.title).assertIsDisplayed()
            onNodeWithText(note.content).assertIsDisplayed()
            onNodeWithText(updateButtonText)
                .assertIsDisplayed()
                .assertIsEnabled()
        }
    }

    @Test
    fun noteForms_shouldDisplayEmpty_whenNoteIsNull() {
        composeTestRule.apply {
            setContent {
                NoteForms(note = null, onClick = mockOnClick)
            }

            onNodeWithTag(noteTitleFieldTag).assertIsDisplayed()
            onNodeWithTag(noteContentFieldTag).assertIsDisplayed()
            onNodeWithText(createButtonText)
                .assertIsDisplayed()
                .assertIsNotEnabled()
        }
    }

    @Test
    fun noteForms_shouldUpdateFields_whenUserTypes() {
        val newTitle = "New Title"
        val newContent = "New Content"

        composeTestRule.apply{
            setContent {
                NoteForms(note = null, onClick = mockOnClick)
            }
            onNodeWithTag(noteTitleFieldTag)
                .performTextInput(newTitle)
            onNodeWithTag(noteContentFieldTag)
                .performTextInput(newContent)

            onNodeWithTag(noteTitleFieldTag)
                .assert(hasEditableText(newTitle))
            onNodeWithTag(noteContentFieldTag)
                .assert(hasEditableText(newContent))
        }
    }

    @Test
    fun noteForms_shouldInvokeOnClickForUpdate_whenButtonClicked() {
        val note = mockNotes.first()
        val updatedTitle = "Updated Title"
        val updatedContent = "Updated Content"

        composeTestRule.apply {
            setContent {
                NoteForms(note = note, onClick = mockOnClick)
            }

            onNodeWithTag(noteTitleFieldTag)
                .performTextInput(updatedTitle)

            onNodeWithTag(noteContentFieldTag)
                .performTextInput(updatedContent)

            onNodeWithText(updateButtonText)
                .performClick()

            verify { mockOnClick(any()) }
        }
    }

    @Test
    fun noteForms_shouldInvokeOnClickForCreate_whenButtonClicked() {
        val newTitle = "New Note Title"
        val newContent = "New Note Content"
        composeTestRule.apply {
            setContent {
                NoteForms(note = null, onClick = mockOnClick)
            }
            onNodeWithTag(noteTitleFieldTag)
                .performTextInput(newTitle)
            onNodeWithTag(noteContentFieldTag)
                .performTextInput(newContent)
            onNodeWithText(createButtonText)
                .performClick()

            verify { mockOnClick(any()) }
        }
    }
}