package br.com.arml.cep.ui.component.favorite.listpane.item.note

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.note.NoteList
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    val ctx = InstrumentationRegistry.getInstrumentation().targetContext!!

    private val noteListTag: String = ctx.getString(R.string.noteList_component_testTag)
    private val noteItemDeleteTag: String = ctx.getString(R.string.noteItem_deleteIconButton_testTag)
    private val noteItemEditTag: String = ctx.getString(R.string.noteItem_component_testTag)

    private val mockOnEditNote: (Note) -> Unit = mockk(relaxed = true)
    private val mockOnDeleteNote: (Note) -> Unit = mockk(relaxed = true)

    @Test
    fun noteList_shouldDisplayAllNoteItems() {
        val notes = mockNotes
        composeTestRule.apply {
            setContent {
                NoteList(
                    notes = notes,
                    onDeleteNote = {},
                    onEditNote = {}
                )
            }
            onNodeWithTag(noteListTag).assertExists().assertIsDisplayed()

            val noteItemDeleteButtons = onAllNodesWithTag(
                noteItemDeleteTag,
                true
            )
            val noteItemEditButtons = onAllNodesWithTag(
                noteItemEditTag,
                true
            )

            notes.forEachIndexed { index, note ->
                onNodeWithText(note.title).assertExists()
                noteItemDeleteButtons[index].assertExists()
                noteItemEditButtons[index].assertExists()
            }
        }
    }

    @Test
    fun noteList_shouldInvokesOnEditCallback_whenCardIsClicked() {
        val notes = mockNotes
        composeTestRule.apply {
            setContent {
                NoteList(
                    notes = notes,
                    onDeleteNote = {},
                    onEditNote = mockOnEditNote
                )
            }

            val noteItemEditButtons = onAllNodesWithTag(
                noteItemEditTag,
                true
            )

            notes.forEachIndexed { index, note ->
                noteItemEditButtons[index].performClick()
                verify(exactly = 1) { mockOnEditNote(note) }
            }
        }
    }

    @Test
    fun noteList_shouldInvokesOnDeleteCallback_whenDeleteIconIsClicked() {
        val notes = mockNotes
        composeTestRule.apply {
            setContent {
                NoteList(
                    notes = notes,
                    onDeleteNote = mockOnDeleteNote,
                    onEditNote = {}
                )
            }

            val noteItemEditButtons = onAllNodesWithTag(
                noteItemDeleteTag,
                true
            )

            notes.forEachIndexed { index, note ->
                noteItemEditButtons[index].performClick()
                verify(exactly = 1) { mockOnDeleteNote(note) }
            }
        }
    }
}