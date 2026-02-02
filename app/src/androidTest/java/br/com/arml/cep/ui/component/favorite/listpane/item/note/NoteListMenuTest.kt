package br.com.arml.cep.ui.component.favorite.listpane.item.note

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.note.NoteListMenu
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListMenuTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    val ctx = InstrumentationRegistry.getInstrumentation().targetContext!!

    val noteListMenuTag = ctx.getString(R.string.noteListMenu_component_testTag)
    val noteListMenuHorizontalDividerTag = ctx.getString(R.string.noteListMenu_horizontalDivider_testTag)
    val noteListVisibilityTag = ctx.getString(R.string.noteListVisibility_component_testTag)
    val noteListMenuAddNoteButtonTag = ctx.getString(R.string.noteListMenu_addNoteButton_testTag)
    val onAddNote: () -> Unit = mockk(relaxed = true)

    @Test
    fun noteListMenu_shouldDisplayNoteListVisibilityHorizontalDividerAndAddNoteButton(){
        composeTestRule.apply{
            setContent {
                NoteListMenu(
                    isShownNotes = false,
                    onChangeShownNotes = {},
                    onAddNote = {}
                )
            }
            onNodeWithTag(noteListMenuTag).assertExists()
            onNodeWithTag(noteListVisibilityTag).assertExists()
            onNodeWithTag(noteListMenuHorizontalDividerTag).assertExists()
            onNodeWithTag(noteListMenuAddNoteButtonTag).assertExists()
        }
    }

    @Test
    fun noteListMenu_shouldInvokesOnAddNoteCallback_whenAddNoteButtonIsClicked(){
        composeTestRule.apply{
            setContent {
                NoteListMenu(
                    isShownNotes = false,
                    onChangeShownNotes = {},
                    onAddNote = onAddNote
                )
            }
            onNodeWithTag(noteListMenuAddNoteButtonTag).performClick()
            verify(exactly = 1) { onAddNote() }
        }
    }
}