package br.com.arml.cep.ui.component.favorite.detailpane.item

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.detailpane.note.NoteFormsButton
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class NoteFormsButtonTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val buttonComponent = ctx.getString(
        R.string.noteFormsButton_component_testTag
    )
    private val saveButtonText = ctx.getString(
        R.string.noteFormsButton_saveButton_text
    )
    private val saveButtonDescription = ctx.getString(
        R.string.noteFormsButton_saveButton_description
    )
    private val updateButtonText = ctx.getString(
        R.string.noteFormsButton_updateButton_text
    )
    private val updateButtonDescription = ctx.getString(
        R.string.noteFormsButton_updateButton_description
    )

    @Test
    fun noteFormsButton_shouldEnableSaveButton_whenTitleIsValid_inSaveMode(){
        val (_, title, content) = mockNotes.first()
        composeTestRule.apply {
            setContent {
                NoteFormsButton(
                    id = 0L,
                    title = title,
                    content = content,
                    onClick = {}
                )
            }
            onNodeWithTag(buttonComponent)
                .assertExists()
                .assertTextEquals(saveButtonText)
                .assert(hasContentDescription(saveButtonDescription))
                .assertIsEnabled()
        }
    }

    @Test
    fun noteFormsButton_shouldUnableSaveButton_whenTitleIsInvalid_inSaveMode(){
        val (_, title, content) = mockNotes.first()
        val invalidTitle = title.take(MIN_TITLE_LENGTH - 1)
        composeTestRule.apply {
            setContent {
                NoteFormsButton(
                    id = 0L,
                    title = invalidTitle,
                    content = content,
                    onClick = {}
                )
            }
            onNodeWithTag(buttonComponent).assertIsNotEnabled()
        }
    }

    @Test
    fun noteFormsButton_shouldPerformsClick_whenButtonIsEnable_inSaveMode(){
        val (_, title, content) = mockNotes.first()
        val expectedNote = Note.build(0L, title, content)
        val mockOnClick: (Note) -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                NoteFormsButton(
                    id = 0L,
                    title = title,
                    content = content,
                    onClick = mockOnClick
                )
            }
            onNodeWithTag(buttonComponent)
                .assertIsEnabled()
                .performClick()
            verify { mockOnClick(expectedNote) }
        }
    }

    @Test
    fun noteFormsButton_shouldEnableSaveButton_whenTitleIsValid_inUpdateMode(){
        val (id, title, content) = mockNotes.first()
        composeTestRule.apply {
            setContent {
                NoteFormsButton(
                    id = id,
                    title = title,
                    content = content,
                    onClick = {}
                )
            }
            onNodeWithTag(buttonComponent)
                .assertExists()
                .assertTextEquals(updateButtonText)
                .assert(hasContentDescription(updateButtonDescription))
                .assertIsEnabled()
        }
    }

    @Test
    fun noteFormsButton_shouldUnableSaveButton_whenTitleIsInvalid_inUpdateMode(){
        val (id, title, content) = mockNotes.first()
        val invalidTitle = title.take(MIN_TITLE_LENGTH - 1)
        composeTestRule.apply {
            setContent {
                NoteFormsButton(
                    id = id,
                    title = invalidTitle,
                    content = content,
                    onClick = {}
                )
            }
            onNodeWithTag(buttonComponent).assertIsNotEnabled()
        }
    }

    @Test
    fun noteFormsButton_shouldPerformsClick_whenButtonIsEnable_inUpdateMode(){
        val expectedNote = mockNotes.first()
        val (id, title, content) = expectedNote
        val mockOnClick: (Note) -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                NoteFormsButton(
                    id = id,
                    title = title,
                    content = content,
                    onClick = mockOnClick
                )
            }
            onNodeWithTag(buttonComponent)
                .assertIsEnabled()
                .performClick()
            verify { mockOnClick(expectedNote) }
        }
    }
}