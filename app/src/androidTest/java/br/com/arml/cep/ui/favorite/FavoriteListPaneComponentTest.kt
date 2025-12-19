package br.com.arml.cep.ui.favorite

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.exception.UnknownException
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.FavoriteListPaneComponent
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import br.com.arml.cep.ui.utils.PlaceFilterOption
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class FavoriteListPaneComponentTest {
    @get:Rule
    val componentTest = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val favoriteListPaneOnLoading = ctx.getString(
        R.string.favoriteListPaneOnLoading_component_testTag
    )
    private val favoriteListPaneOnFailure = ctx.getString(
        R.string.favoriteListPaneOnFailure_component_testTag
    )
    private val favoriteListPaneOnSuccess = ctx.getString(
        R.string.favoriteListPaneOnSuccess_component_testTag
    )
    private val placeFilterComponent = ctx.getString(
        R.string.favoriteListPaneComponent_placeFilterComponent_testTag
    )
    private val favoriteListComponent = ctx.getString(
        R.string.favoriteListPaneComponent_favoriteListComponent_testTag
    )

    private val loadingState = FavoriteState()
    private val failureState = FavoriteState(
        fetchEntries = Response.Failure(
            UnknownException.FetchPlaceException()
        )
    )
    private val successState = FavoriteState(
        fetchEntries = Response.Success(mockFavoritePlaces)
    )

    @Test
    fun favoriteListPaneComponent_shouldDisplayLoadingContent_whenStateIsLoading(){
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(state = loadingState)
            }
            onNodeWithTag(favoriteListPaneOnLoading).assertIsDisplayed()
            onNodeWithTag(placeFilterComponent).assertDoesNotExist()
            onNodeWithTag(favoriteListComponent).assertDoesNotExist()
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldDisplayFailureContent_whenStateIsFailure(){
        val expectedMsg = UnknownException.FetchPlaceException().message
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(state = failureState)
            }
            onNodeWithTag(favoriteListPaneOnFailure).assertIsDisplayed()
            onNodeWithTag(favoriteListPaneOnSuccess).assertIsNotDisplayed()
            onNodeWithTag(favoriteListPaneOnLoading).assertIsNotDisplayed()

            onNodeWithText(expectedMsg).assertIsDisplayed()
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldDisplaySuccessContent_whenStateIsSuccess(){
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(state = successState)
            }
            onNodeWithTag(favoriteListPaneOnSuccess).assertIsDisplayed()
            onNodeWithTag(favoriteListPaneOnLoading).assertIsNotDisplayed()
            onNodeWithTag(favoriteListPaneOnFailure).assertIsNotDisplayed()

            onNodeWithTag(placeFilterComponent).assertIsDisplayed()
            onNodeWithTag(favoriteListComponent).assertIsDisplayed()
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldDisplaySnackbar_whenSnackbarMsgIsReceived(){
        val expectedSnackbarMsg = "Message"
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    snackbarMsg = expectedSnackbarMsg
                )
            }
            onNodeWithText(expectedSnackbarMsg).assertIsDisplayed()
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnImportClick_whenImportButtonIsClicked(){
        val importButton = ctx.getString(
            R.string.favoriteListPaneHeaderMenu_importButton_testTag
        )
        val mockOnImportClick: () -> Unit = mockk(relaxed = true)
        componentTest.apply {
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onImportClick = mockOnImportClick
                )
            }
            onNodeWithTag(importButton).performClick()
            verify(exactly = 1) { mockOnImportClick() }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnExportClick_whenExportButtonIsClicked(){
        val exportButton = ctx.getString(
            R.string.favoriteListPaneHeaderMenu_exportButton_testTag
        )
        val mockOnExportClick: () -> Unit = mockk(relaxed = true)
        componentTest.apply {
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onExportClick = mockOnExportClick
                )
            }
            onNodeWithTag(exportButton).performClick()
            verify(exactly = 1) { mockOnExportClick() }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnCepFilterCallback_whenCepFilterIsSelectedAndFilterButtonIsClicked(){
        val filterField = ctx.getString(
            R.string.searchCepField_component_testTag
        )
        val filterButton = ctx.getString(
            R.string.cepFilter_filterButton_testTag
        )
        val filterOption = PlaceFilterOption.ByCep.name

        val query = "11111111"
        val expectedQuery = "11111-111"
        val mockOnCepFilter: (String) -> Unit = mockk(relaxed = true)
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onCepFilter = mockOnCepFilter
                )
            }
            onNodeWithText(filterOption).performClick()
            onNodeWithTag(filterField).performTextInput(query)
            onNodeWithTag(filterButton).performClick()
            verify(exactly = 1) { mockOnCepFilter(expectedQuery) }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnTitleFilterCallback_whenTitleFilterIsSelectedAndFilterButtonIsClicked(){
        val filterField = ctx.getString(
            R.string.titleFilter_titleField_testTag
        )
        val filterButton = ctx.getString(
            R.string.titleFilter_filterButton_testTag
        )
        val filterOption = PlaceFilterOption.ByTitle.name

        val query = "query"
        val expectedQuery = "query"
        val mockOnTitleFilter: (String) -> Unit = mockk(relaxed = true)
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onTitleFilter = mockOnTitleFilter
                )
            }
            onNodeWithText(filterOption).performClick()
            onNodeWithTag(filterField).performTextInput(query)
            onNodeWithTag(filterButton).performClick()
            verify(exactly = 1) { mockOnTitleFilter(expectedQuery) }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnNoneFilterCallback_whenNoneFilterIsSelectedAndFilterButtonIsClicked(){
        val cepFilterOption = PlaceFilterOption.ByCep.name
        val noneFilterOption = PlaceFilterOption.None.name

        val mockOnNoneFilter: () -> Unit = mockk(relaxed = true)
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onNoneFilter = mockOnNoneFilter
                )
            }
            onNodeWithText(cepFilterOption).performClick()
            onNodeWithText(noneFilterOption).performClick()
            verify(exactly = 2) { mockOnNoneFilter() }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnAddNoteCallback_whenAddNoteButtonIsClicked() {
        val place = mockFavoritePlaces.first()
        val expectedCep = place.cep
        val expectedNote = Note.build(
            title = expectedCep.text,
            content = ""
        )
        val mockOnAddNote: (Cep, Note) -> Unit = mockk(relaxed = true)
        val addNoteButtonTag = ctx.getString(R.string.noteListMenu_addNoteButton_testTag)

        componentTest.apply {
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onAddNote = mockOnAddNote
                )
            }

            onAllNodesWithTag(addNoteButtonTag)[0].performClick()

            verify(exactly = 1) { mockOnAddNote(expectedCep, expectedNote) }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnFavoriteIconClickCallback_whenFavoriteButtonIsClicked() {
        val expectedPlace = mockFavoritePlaces.first()
        val zipcode = expectedPlace.cep.text
        val deleteButtonDescription = ctx.getString(
            R.string.favoriteItemActionBar_favoriteIcon_testTag,
            zipcode
        )
        val mockOnFavoriteIconClick: (Place) -> Unit = mockk(relaxed = true)
        componentTest.apply {
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onFavoriteIconClick = mockOnFavoriteIconClick
                )
            }
            onNodeWithTag(deleteButtonDescription).performClick()
            verify(exactly = 1) { mockOnFavoriteIconClick(expectedPlace) }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnDeleteNoteCallback_whenDeleteButtonIsClicked() {
        val cep = mockFavoritePlaces.first().cep
        val note = mockFavoritePlaces.first().notes.first()

        val showNotesButtonText = ctx.getString(
            R.string.noteListVisibility_showList_Text
        )
        val deleteButtonLabel = ctx.getString(
            R.string.noteItem_deleteIconButton_description,
            note.title
        )

        val mockOnDeleteNote: (Pair<Cep, Note>) -> Unit = mockk(relaxed = true)
        componentTest.apply {
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onDeleteNote = mockOnDeleteNote
                )
            }
            onAllNodesWithText(showNotesButtonText)[0].performClick()
            //onNodeWithText(note.title).performClick()
            onNode(hasContentDescription(deleteButtonLabel)).performClick()
            verify(exactly = 1) { mockOnDeleteNote(cep to note) }
        }
    }

    @Test
    fun favoriteListPaneComponent_shouldInvokesOnNavigateToDetailsCallback_whenNoteIsClicked(){
        val expectedData = mockFavoritePlaces.first().let { it.address to it.notes.first() }
        val noteTitle = expectedData.second.title
        val showNotesButtonText = ctx.getString(R.string.noteListVisibility_showList_Text)
        val mockOnNavigateToDetails: (Pair<Address, Note>) -> Unit = mockk(relaxed = true)
        componentTest.apply{
            setContent {
                FavoriteListPaneComponent(
                    state = successState,
                    onNavigateToDetails = mockOnNavigateToDetails
                )
            }
            onAllNodesWithText(showNotesButtonText)[0].performClick()
            onNodeWithText(noteTitle).performClick()
            verify(exactly = 1) { mockOnNavigateToDetails(expectedData) }
        }
    }
}