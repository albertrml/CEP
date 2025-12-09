package br.com.arml.cep.ui.component.favorite.listpane.item.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.R.string.noteListVisibility_hideIcon_description
import br.com.arml.cep.R.string.noteListVisibility_hideText
import br.com.arml.cep.R.string.noteListVisibility_showIcon_description
import br.com.arml.cep.R.string.noteListVisibility_showText
import br.com.arml.cep.ui.screen.component.favorite.listpane.item.note.NoteListVisibility
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListVisibilityTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    val ctx = InstrumentationRegistry.getInstrumentation().targetContext!!

    private val noteListVisibilityTag = ctx.getString(
        R.string.noteListVisibility_component_testTag
    )
    private val noteListVisibilityHideText = ctx.getString(
        noteListVisibility_hideText
    )
    private val noteListVisibilityShowText = ctx.getString(
        noteListVisibility_showText
    )
    private val noteListVisibilityShowIconDescription = ctx.getString(
        noteListVisibility_showIcon_description
    )
    private val noteListVisibilityHideIconDescription = ctx.getString(
        noteListVisibility_hideIcon_description
    )

    @Test
    fun noteListVisibility_shouldDisplayShowTextAndShowIcon(){
        var isShownNotes by mutableStateOf(false)
        composeTestRule.apply {
            setContent {
                NoteListVisibility(
                    isShownNotes = isShownNotes,
                    onChangeShownNotes = {}
                )
            }
            onNodeWithTag(noteListVisibilityTag).assertExists()
            onNodeWithText(noteListVisibilityShowText).assertExists()
            onNodeWithContentDescription(noteListVisibilityShowIconDescription).assertExists()
        }
    }

    @Test
    fun noteListVisibility_shouldDisplayHideTextAndHideIcon(){
        var isShownNotes by mutableStateOf(true)
        composeTestRule.apply {
            setContent {
                NoteListVisibility(
                    isShownNotes = isShownNotes,
                    onChangeShownNotes = {}
                )
            }
            onNodeWithTag(noteListVisibilityTag).assertExists()
            onNodeWithText(noteListVisibilityHideText).assertExists()
            onNodeWithContentDescription(noteListVisibilityHideIconDescription).assertExists()
        }
    }

    @Test
    fun noteListVisibility_shouldDisplayHideTextAndHideIcon_whenNoteListVisibilityIsClicked(){
        var isShownNotes by mutableStateOf(false)
        val onChangeShownNotes: () -> Unit = { isShownNotes = !isShownNotes }
        composeTestRule.apply {
            setContent {
                NoteListVisibility(
                    isShownNotes = isShownNotes,
                    onChangeShownNotes = onChangeShownNotes
                )
            }
            onNodeWithTag(noteListVisibilityTag).performClick()
            onNodeWithText(noteListVisibilityHideText).assertExists()
            onNodeWithContentDescription(noteListVisibilityHideIconDescription).assertExists()
        }
    }

    @Test
    fun noteListVisibility_shouldDisplayShowTextAndShowIcon_whenNoteListVisibilityIsClickedTwice(){
        var isShownNotes by mutableStateOf(false)
        val onChangeShownNotes: () -> Unit = { isShownNotes = !isShownNotes }
        composeTestRule.apply {
            setContent {
                NoteListVisibility(
                    isShownNotes = isShownNotes,
                    onChangeShownNotes = onChangeShownNotes
                )
            }
            onNodeWithTag(noteListVisibilityTag).performClick()
            onNodeWithTag(noteListVisibilityTag).performClick()
            onNodeWithText(noteListVisibilityShowText).assertExists()
            onNodeWithContentDescription(noteListVisibilityShowIconDescription).assertExists()
        }
    }
}