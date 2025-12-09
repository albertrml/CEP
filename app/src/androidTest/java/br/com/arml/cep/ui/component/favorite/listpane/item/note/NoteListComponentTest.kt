package br.com.arml.cep.ui.component.favorite.listpane.item.note

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.note.NoteListComponent
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class NoteListComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val ctx = InstrumentationRegistry.getInstrumentation().targetContext!!

    private val noteListComponentTag = ctx
        .getString(R.string.noteListComponent_component_testTag)
    private val noteListVisibility = ctx
        .getString(R.string.noteListVisibility_component_testTag)

    @Test
    fun noteListComponent_shouldDisplayOnlyNoteListMenu_whenNoteListIsHidden(){
        composeTestRule.apply {
            setContent {
                NoteListComponent(
                    notes = mockNotes,
                    onDeleteNote = {},
                    onEditNote = {},
                    onAddNote = {}
                )
            }

            onNodeWithTag(noteListComponentTag).assertExists()

            mockNotes.forEach {
                composeTestRule.onNodeWithText(it.title, useUnmergedTree = true)
                    .assertIsNotDisplayed()
            }
        }
    }

    @Test
    fun noteListComponent_shouldDisplayNoteListMenuAndNoteList_whenNoteListVisibilityIsClicked(){
        composeTestRule.apply {
            setContent {
                NoteListComponent(
                    notes = mockNotes,
                    onDeleteNote = {},
                    onEditNote = {},
                    onAddNote = {}
                )
            }
            onNodeWithTag(noteListVisibility).performClick()
            mockNotes.forEach {
                composeTestRule.onNodeWithText(it.title, useUnmergedTree = true)
                    .assertExists()
                    .assertIsDisplayed()
            }
        }
    }
}