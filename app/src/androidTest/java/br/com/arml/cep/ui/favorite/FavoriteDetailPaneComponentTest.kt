package br.com.arml.cep.ui.favorite

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.model.mock.mockNotes
import br.com.arml.cep.ui.screen.component.favorite.FavoriteDetailPaneComponent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDetailPaneComponentTest {
    @get:Rule
    val componentTest = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val noteTabLabel = ctx.getString(R.string.favoriteTab_noteTab_label)
    private val addressTabLabel = ctx.getString(R.string.favoriteTab_addressTab_label)
    private val noteFormsComponent = ctx.getString(R.string.noteForms_component_testTag)
    private val addressFormsComponent = ctx.getString(R.string.addressForms_component_testTag)
    private val noteFormsButtonComponent = ctx.getString(R.string.noteFormsButton_component_testTag)
    private val noteTitleFieldComponent = ctx.getString(R.string.noteTitleField_component_testTag)
    private val noteContentFieldComponent = ctx.getString(R.string.noteContentField_component_testTag)

    private val mockOnEditNote: (Note) ->  Unit = mockk(relaxed = true)
    private val mockOnCreateNote: (Cep, Note) -> Unit = mockk(relaxed = true)
    private val mockFavoriteForEdit: Pair<Address, Note> = mockAddress(1) to mockNotes.first()
    private val mockFavoriteForCreation: Pair<Address, Note?> = mockAddress(1) to null

    @Test
    fun favoriteDetailPaneComponent_shouldNavigateBetweenTabs_whenTabIsClicked(){
        componentTest.apply{
            setContent {
                FavoriteDetailPaneComponent(
                    favorite = mockFavoriteForEdit,
                    onNavigateBackToList = {},
                    onEditNote = mockOnEditNote,
                    onCreateNote = mockOnCreateNote
                )
            }
            val tabNodes = onAllNodes(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.Role,
                    Role.Tab)
            )

            tabNodes
                .filterToOne(hasText(noteTabLabel))
                .performClick()
            onNodeWithTag(noteFormsComponent).assertIsDisplayed()

            tabNodes
                .filterToOne(hasText(addressTabLabel))
                .performClick()
            onNodeWithTag(addressFormsComponent).assertIsDisplayed()
        }
    }

    @Test
    fun favoriteDetailPaneComponent_shouldInvokesOnCreateNoteCallback_whenCreateButtonIsClicked(){
        val (address,_) = mockFavoriteForCreation
        val expectedCep = Cep.build(address.zipCode)
        val expectedNote = Note.build(
            id = 0L,
            title = mockNotes.first().title,
            content = mockNotes.first().content
        )
        componentTest.apply{
            setContent {
                FavoriteDetailPaneComponent(
                    favorite = mockFavoriteForCreation,
                    onNavigateBackToList = {},
                    onEditNote = mockOnEditNote,
                    onCreateNote = mockOnCreateNote
                )
            }
            val tabNodes = onAllNodes(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.Role,
                    Role.Tab)
            )

            tabNodes
                .filterToOne(hasText(noteTabLabel))
                .performClick()

            onNodeWithTag(noteTitleFieldComponent)
                .performTextInput(expectedNote.title)

            onNodeWithTag(noteContentFieldComponent)
                .performTextInput(expectedNote.content)

            onNodeWithTag(noteFormsButtonComponent)
                .assertIsDisplayed()
                .performClick()

            verify { mockOnCreateNote(expectedCep, expectedNote) }
        }
    }

    @Test
    fun favoriteDetailPaneComponent_shouldInvokesOnEditNoteCallback_whenUpdateButtonIsClicked(){
        val (address, _) = mockFavoriteForEdit
        val expectedCep = Cep.build(address.zipCode)
        val expectedNote = Note.build(
            id = 0L,
            title = mockNotes.last().title,
            content = mockNotes.last().content
        )
        componentTest.apply{
            setContent {
                FavoriteDetailPaneComponent(
                    favorite = mockFavoriteForCreation,
                    onNavigateBackToList = {},
                    onEditNote = mockOnEditNote,
                    onCreateNote = mockOnCreateNote
                )
            }
            val tabNodes = onAllNodes(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.Role,
                    Role.Tab)
            )

            tabNodes
                .filterToOne(hasText(noteTabLabel))
                .performClick()

            onNodeWithTag(noteTitleFieldComponent)
                .performTextInput(expectedNote.title)

            onNodeWithTag(noteContentFieldComponent)
                .performTextInput(expectedNote.content)

            onNodeWithTag(noteFormsButtonComponent)
                .assertIsDisplayed()
                .performClick()

            verify { mockOnCreateNote(expectedCep, expectedNote) }
        }
    }
}