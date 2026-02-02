package br.com.arml.cep.ui.component.favorite.listpane.item.note

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.note.NoteItem
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val note = mockNotes.first()
    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val noteItemTestTag = ctx.getString(
        R.string.noteItem_component_testTag,
        note.id
    )

    private val noteItemDeleteButtonTestTag = ctx.getString(R.string.noteItem_deleteIconButton_testTag)

    private val noteItemTitleTestTag = ctx.getString(R.string.noteItem_titleText_testTag)

    @Test
    fun noteItem_whenRendered_displaysTitleAndActionButtons(){
        composeTestRule.setContent {
            NoteItem(
                note = note,
                onDeleteNote = {},
                onEditNote = {}
            )
        }

        composeTestRule.onNodeWithTag(noteItemTestTag)
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNodeWithTag(noteItemTitleTestTag, useUnmergedTree = true)
            .assertTextEquals(note.title)

        composeTestRule.onNodeWithTag(noteItemDeleteButtonTestTag, useUnmergedTree = true)
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun noteItem_whenClicked_invokesOnEditCallback(){
        val note = mockNotes.first()
        val onEditNote: (Note) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            NoteItem(
                note = note,
                onDeleteNote = {},
                onEditNote = onEditNote
            )
        }

        composeTestRule.onNodeWithTag(noteItemTestTag).performClick()

        verify(exactly = 1) { onEditNote(note) }
    }

    @Test
    fun noteItem_whenDeleteIconClicked_invokesOnDeleteCallback(){
        val note = mockNotes.first()
        val onDeleteNote: (Note) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            NoteItem(
                note = note,
                onDeleteNote = onDeleteNote,
                onEditNote = {}
            )
        }

        composeTestRule.onNodeWithTag(noteItemDeleteButtonTestTag).performClick()

        verify(exactly = 1) { onDeleteNote(note) }
    }
}