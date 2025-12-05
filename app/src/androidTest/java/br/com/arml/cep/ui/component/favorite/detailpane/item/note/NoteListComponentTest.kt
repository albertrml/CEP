package br.com.arml.cep.ui.component.favorite.detailpane.item.note

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
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
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class NoteListComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val onEditNote: (Note) -> Unit = mockk(relaxed = true)
    private val onDeleteNote: (Note) -> Unit = mockk(relaxed = true)

    @Test
    fun noteListComponent_should(){
        composeTestRule.setContent {
            NoteList(
                notes = mockNotes,
                onDeleteNote = {},
                onEditNote = {}
            )
        }

        mockNotes.forEach {
            composeTestRule.onNodeWithText(it.title, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun noteListComponent_shouldInvokesOnEditCallback_whenNoteClicked() {
        composeTestRule.setContent {
            NoteList(
                notes = mockNotes,
                onDeleteNote = {},
                onEditNote = onEditNote
            )
        }

        mockNotes.forEach { item ->
            composeTestRule
                .onNodeWithText(item.title, useUnmergedTree = true)
                .performClick()
            verify(exactly = 1) { onEditNote(item) }
        }
    }

    @Test
    fun noteListComponent_shouldInvokesOnDeleteCallback_whenNoteClicked() {
        val noteItemDeleteButton = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.noteItem_deleteIconButton_testTag)

        composeTestRule.setContent {
            NoteList(
                notes = mockNotes,
                onDeleteNote = onDeleteNote,
                onEditNote = {}
            )
        }

        val deleteButtonNodes = composeTestRule
            .onAllNodes(hasTestTag(noteItemDeleteButton))

        mockNotes.forEachIndexed { index, item ->
            deleteButtonNodes[index].performClick()
            verify(exactly = 1) { onDeleteNote(item) }
        }
    }
}