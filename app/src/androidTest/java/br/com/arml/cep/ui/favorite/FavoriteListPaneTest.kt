package br.com.arml.cep.ui.favorite

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.mock.mockFavoritePlaceEntries
import br.com.arml.cep.ui.screen.component.favorite.FavoriteListComponent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteListPaneTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

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

    private val onImportClick: () -> Unit = mockk(relaxed = true)
    private val onExportClick: () -> Unit = mockk(relaxed = true)
    private val onFavoriteIconClick: (PlaceEntry) -> Unit = mockk(relaxed = true)
    private val onCepFilter: (String) -> Unit = mockk(relaxed = true)
    private val onTitleFilter: (String) -> Unit = mockk(relaxed = true)
    private val onNoneFilter: () -> Unit = mockk(relaxed = true)
    private val onNavigateToDetails: (PlaceEntry) -> Unit = mockk(relaxed = true)

    private val placeFavoriteEntry = mockFavoritePlaceEntries.random()

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

    fun displayFavoriteListPaneTest(response: Response<List<PlaceEntry>>){
        composeTestRule.setContent {
            FavoriteListComponent(
                modifier = Modifier.testTag(favoriteListPaneComponent),
                fetchResponse = response,
                onImportClick = onImportClick,
                onExportClick = onExportClick,
                onFavoriteIconClick = onFavoriteIconClick,
                onCepFilter = onCepFilter,
                onTitleFilter = onTitleFilter,
                onNoneFilter = onNoneFilter,
                onNavigateToDetails = onNavigateToDetails
            )
        }
    }

    @Test
    fun shouldDisplayOnLoadingComponents_whenResponseIsLoading(){
        displayFavoriteListPaneTest(Response.Loading)
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
        displayFavoriteListPaneTest(Response.Success(mockFavoritePlaceEntries))
        mockFavoritePlaceEntries.forEach { place ->
            composeTestRule.apply {
                onNodeWithTag(favoriteListPaneComponent).performScrollToNode(
                    hasText(place.note!!.title)
                )
                onNodeWithText(place.note.title, substring = true).assertExists()
                onNodeWithText(place.cep.toFormattedCep(), substring = true).assertExists()
            }
        }
    }

    @Test
    fun shouldDisplayOnFailureComponents_whenResponseIsFailure(){
        val msg = "Error"
        displayFavoriteListPaneTest(Response.Failure(Exception(msg)))
        composeTestRule
            .onNodeWithTag(favoriteListPaneOnFailure)
            .assertTextEquals(msg)
    }

    @Test
    fun shouldNavigateToDetails_whenFavoritePlaceEntryIsClicked(){
        val place = mockFavoritePlaceEntries.random()
        displayFavoriteListPaneTest(Response.Success(mockFavoritePlaceEntries))
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneComponent).performScrollToNode(
                hasText(place.note!!.title)
            )
            onNodeWithText(place.note.title, substring = true).performClick()
            verify { onNavigateToDetails(place) }
        }
    }

    @Test
    fun shouldUnfavoritePlaceEntry_whenFavoriteIconIsClicked(){
        displayFavoriteListPaneTest(Response.Success(mockFavoritePlaceEntries))
        composeTestRule.apply {
            onRoot().printToLog("FavoriteListPaneTest")
            onNodeWithTag(favoriteListPaneComponent)
                .performScrollToNode(hasText(placeFavoriteEntry.note!!.title))
            onNodeWithTag(favoriteListPaneIconButton).performClick()
            verify { onFavoriteIconClick(placeFavoriteEntry) }
        }
    }

    @Test
    fun shouldImportFavoritePlaceEntries_whenImportButtonIsClicked(){
        displayFavoriteListPaneTest(Response.Success(mockFavoritePlaceEntries))
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneHeaderImportButton).performClick()
            verify { onImportClick() }
        }
    }

    @Test
    fun shouldExportFavoritePlaceEntries_whenExportButtonIsClicked(){
        displayFavoriteListPaneTest(Response.Success(mockFavoritePlaceEntries))
        composeTestRule.apply {
            onNodeWithTag(favoriteListPaneHeaderExportButton).performClick()
            verify { onExportClick() }
        }
    }
}