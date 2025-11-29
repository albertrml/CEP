package br.com.arml.cep.ui.favorite

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteListPaneTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    val loadingState = FavoriteState()

    private lateinit var favoriteListPaneComponent: String
    private lateinit var favoriteListPaneHeader: String
    private lateinit var favoriteListPaneList: String
    private lateinit var favoriteListPaneFilter: String
    private lateinit var favoriteListPaneOnSuccess: String
    private lateinit var favoriteListPaneOnLoading: String
    private lateinit var favoriteListPaneOnFailure: String
    private lateinit var favoriteListPaneIconButton: String
    private lateinit var favoriteListPaneHeaderImportButton: String
    private lateinit var favoriteListPaneHeaderExportButton: String

    private val mockOnImportClick: () -> Unit = mockk(relaxed = true)
    private val mockOnExportClick: () -> Unit = mockk(relaxed = true)
    private val mockOnCepFilter: (String) -> Unit = mockk(relaxed = true)
    private val mockOnTitleFilter: (String) -> Unit = mockk(relaxed = true)
    private val mockOnNoneFilter: () -> Unit = mockk(relaxed = true)
    private val mockOnAddNote: (Cep, Note) -> Unit = mockk(relaxed = true)
    private val mockOnFavoriteIconClick: (Place) -> Unit = mockk(relaxed = true)
    private val mockOnDeletePlace: (Pair<Cep, Note>) -> Unit = mockk(relaxed = true)
    private val mockOnNavigateToDetails: (Pair<Address, Note>) -> Unit = mockk(relaxed = true)

    private val placeFavoriteEntry = mockFavoritePlaces.random()

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            favoriteListPaneComponent = getString(R.string.testTag_favoriteList_component)
            favoriteListPaneHeader = getString(R.string.testTag_favoriteList_header)
            favoriteListPaneList = getString(R.string.testTag_favoriteList_list)
            favoriteListPaneFilter = getString(R.string.testTag_favoriteList_filter)
            favoriteListPaneOnSuccess = getString(R.string.testTag_favoriteList_onSuccess)
            favoriteListPaneOnLoading = getString(R.string.testTag_favoriteList_onLoading)
            favoriteListPaneOnFailure = getString(R.string.testTag_favoriteList_onFailure)
            favoriteListPaneIconButton = getString(
                R.string.testTag_placeElement_favoriteIconButton,
                placeFavoriteEntry.cep.text
            )
            favoriteListPaneHeaderImportButton = getString(R.string.testTag_favoriteListHeader_importButton)
            favoriteListPaneHeaderExportButton = getString(R.string.testTag_favoriteListHeader_exportButton)
        }
    }

    fun displayFavoriteListPaneTest(favoriteState: FavoriteState){
        composeTestRule.setContent {
            /*FavoriteDetailPane(
                modifier = Modifier.testTag(favoriteListPaneComponent),
                state = favoriteState,
                onImportClick = mockOnImportClick,
                onExportClick = mockOnExportClick,
                onCepFilter = mockOnCepFilter,
                onTitleFilter = mockOnTitleFilter,
                onNoneFilter = mockOnNoneFilter,
                onAddNote = mockOnAddNote,
                onFavoriteIconClick = mockOnFavoriteIconClick,
                onDeleteNote = mockOnDeletePlace,
                onNavigateToDetails = mockOnNavigateToDetails
            )*/
        }
    }

    @Test
    fun shouldDisplayOnLoadingComponents_whenResponseIsLoading(){
        displayFavoriteListPaneTest(loadingState)
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneComponent).assertExists()
            onNodeWithTag(favoriteListPaneHeader).assertExists()
            onNodeWithTag(favoriteListPaneOnLoading).assertExists()
            onNodeWithTag(favoriteListPaneOnFailure).assertDoesNotExist()
            onNodeWithTag(favoriteListPaneOnSuccess).assertDoesNotExist()
            onNodeWithTag(favoriteListPaneList).assertDoesNotExist()
            onNodeWithTag(favoriteListPaneFilter).assertDoesNotExist()
        }
    }

    @Test
    fun shouldDisplayOnSuccessComponents_whenResponseIsSuccess(){
        /*displayFavoriteListPaneTest(Response.Success(mockFavoritePlaces))
        mockFavoritePlaces.forEach { place ->
            composeTestRule.apply {
                onNodeWithTag(favoriteListPaneComponent).performScrollToNode(
                    hasText(place.notes!!.title)
                )
                onNodeWithText(place.notes.title, substring = true).assertExists()
                onNodeWithText(place.cep.toFormattedCep(), substring = true).assertExists()

            }
        }*/
    }

    @Test
    fun shouldDisplayOnFailureComponents_whenResponseIsFailure(){
        val msg = "Test Exception"
        val failureState = FavoriteState(
            fetchEntries = Response.Failure(Exception(msg))
        )
        displayFavoriteListPaneTest(failureState)
        composeTestRule
            .onNodeWithTag(favoriteListPaneOnFailure)
            .assertTextEquals(msg)
    }

    @Test
    fun shouldNavigateToDetails_whenFavoritePlaceEntryIsClicked(){
        /*val place = mockFavoritePlaces.random()
        displayFavoriteListPaneTest(Response.Success(mockFavoritePlaces))
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneComponent).performScrollToNode(
                hasText(place.notes!!.title)
            )
            onNodeWithText(place.notes.title, substring = true).performClick()
            verify { onNavigateToDetails(place) }
        }*/
    }

    @Test
    fun shouldUnfavoritePlaceEntry_whenFavoriteIconIsClicked(){
        /*displayFavoriteListPaneTest(Response.Success(mockFavoritePlaces))
        composeTestRule.apply {
            onRoot().printToLog("FavoriteListPaneTest")
            onNodeWithTag(favoriteListPaneComponent)
                .performScrollToNode(hasText(placeFavoriteEntry.notes!!.title))
            onNodeWithTag(favoriteListPaneIconButton).performClick()
            verify { onFavoriteIconClick(placeFavoriteEntry) }
        }*/
    }

    @Test
    fun shouldImportFavoritePlaceEntries_whenImportButtonIsClicked(){
        val mockState = FavoriteState(fetchEntries = Response.Success(mockFavoritePlaces))
        displayFavoriteListPaneTest(mockState)
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneHeaderImportButton).performClick()
            verify { mockOnImportClick() }
        }
    }

    @Test
    fun shouldExportFavoritePlaceEntries_whenExportButtonIsClicked(){
        val mockState = FavoriteState(fetchEntries = Response.Success(mockFavoritePlaces))
        displayFavoriteListPaneTest(mockState)
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneHeaderExportButton).performClick()
            verify { mockOnExportClick() }
        }
    }
}