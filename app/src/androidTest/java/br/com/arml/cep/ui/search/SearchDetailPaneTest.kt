package br.com.arml.cep.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.search.SearchDetailPane
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchDetailPaneTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /*** Header ***/
    private lateinit var searchDetailPaneHeader: String
    private lateinit var searchDetailPaneTitleHeader: String
    private lateinit var searchDetailPaneIconHeader: String
    private lateinit var backButton: String
    private val mockOnBackPress: () -> Unit = mockk()

    /*** On Success ***/
    private lateinit var mockPlaceEntry: PlaceEntry
    private lateinit var searchDetailPaneOnSuccess: String

    /*** On Loading ***/
    private lateinit var searchDetailPaneOnLoading: String
    private lateinit var searchDetailPaneOnLoadingCircularProgressIndicator: String

    /*** On Failure ***/
    private lateinit var searchDetailPaneOnFailure: String

    /*** Favorite Button ***/
    private lateinit var searchDetailFavoriteButton: String
    private val mockOnFavorite: (PlaceEntry) -> Unit = mockk()


    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        /*** Header ***/
        searchDetailPaneHeader = context.getString(
            R.string.testTag_searchScreen_detailPane_header)
        searchDetailPaneTitleHeader = context.getString(R.string.testTag_header_title)
        searchDetailPaneIconHeader = context.getString(R.string.testTag_header_icon)
        backButton = context.getString(R.string.icon_button_tag)
        every { mockOnBackPress() } answers { println("mockOnBackPress CALLED") }

        /*** On Loading ***/
        searchDetailPaneOnLoading = context.getString(
            R.string.testTag_searchScreen_detailPane_onLoading)
        searchDetailPaneOnLoadingCircularProgressIndicator = context.getString(
            R.string.testTag_searchScreen_detailPane_onLoading_circularProgressIndicator)

        /*** On Failure ***/
        searchDetailPaneOnFailure = context.getString(R.string.testTag_searchScreen_detailPane_onFailure)

        /*** On Success ***/
        mockPlaceEntry = mockPlaceEntries[0]
        searchDetailPaneOnSuccess = context.getString(R.string.testTag_searchScreen_detailPane_onSuccess)

        /*** Favorite Button ***/
        searchDetailFavoriteButton = context.getString(R.string.testTag_searchScreen_detailPane_saveAddressButton)
        every { mockOnFavorite(any()) } answers {  println("mockOnFavorite CALLED") }
    }

    private fun setDisplayScreenContent(response: Response<PlaceEntry>) {
        composeTestRule.setContent {
            SearchDetailPane(
                modifier = Modifier.fillMaxSize(),
                response = response,
                onBackPress = mockOnBackPress,
                onFavoriteClick = mockOnFavorite
            )
        }
    }

    /*** Header ***/
    @Test
    fun shouldDisplayTitleAndIconOnHeader_whenSearchScreenDetailPaneIsCalled(){
        setDisplayScreenContent(Response.Loading)
        composeTestRule.apply{
            onNodeWithTag(searchDetailPaneHeader).assertExists()
            onNodeWithTag(searchDetailPaneTitleHeader).assertExists()
            onNodeWithTag(searchDetailPaneIconHeader).assertExists()
        }
    }

    @Test
    fun shouldPerformBackNavigation_whenHeaderIconIsClicked(){
        setDisplayScreenContent(Response.Loading)
        composeTestRule.apply{
            onNodeWithTag(searchDetailPaneHeader).assertExists()
            onNodeWithTag(searchDetailPaneIconHeader).assertExists()
            onNodeWithTag(searchDetailPaneIconHeader).performClick()
        }
        verify { mockOnBackPress() }
    }

    /*** Address Information ***/
    @Test
    fun shouldDisplayAddressInformation_whenSearchCepSucceeds(){
        setDisplayScreenContent(Response.Success(mockPlaceEntry))
        composeTestRule.apply {
            onNodeWithTag(searchDetailPaneOnSuccess).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneOnLoading).assertIsNotDisplayed()
            onNodeWithTag(searchDetailPaneOnFailure).assertIsNotDisplayed()
        }
    }

    @Test
    fun shouldDisplayLoadingIndicator_whenSearchCepIsInProgress(){
        setDisplayScreenContent(Response.Loading)
        composeTestRule.apply {
            onNodeWithTag(searchDetailPaneOnSuccess).assertIsNotDisplayed()
            onNodeWithTag(searchDetailPaneOnLoading).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneOnFailure).assertIsNotDisplayed()

            onNodeWithTag(searchDetailPaneOnLoadingCircularProgressIndicator)
                .assertIsDisplayed()
        }
    }

    @Test
    fun shouldDisplayErrorMessage_whenSearchCepFails(){
        val failure = CepException.NotFoundCepException()
        setDisplayScreenContent(Response.Failure(failure))
        composeTestRule.apply {
            onNodeWithTag(searchDetailPaneOnSuccess).assertIsNotDisplayed()
            onNodeWithTag(searchDetailPaneOnLoading).assertIsNotDisplayed()
            onNodeWithTag(searchDetailPaneOnFailure).assertIsDisplayed()

            onNodeWithTag(searchDetailPaneOnFailure).assertExists()
            onNodeWithText(failure.message).assertIsDisplayed()
        }
    }

    /*** Save Button ***/
    @Test
    fun shouldSaveAddress_whenSaveButtonIsClicked(){
        setDisplayScreenContent(Response.Success(mockPlaceEntry))
        composeTestRule.onNodeWithTag(searchDetailFavoriteButton).apply {
            assertExists()
            assertIsEnabled()
            performClick()
        }
    }

    @Test
    fun shouldBeUnableFavoriteButton_whenAddressIsAlreadyFavorite(){
        val mockPlaceEntry = mockPlaceEntries[0].copy(
            isFavorite = Favorite(true)
        )
        setDisplayScreenContent(Response.Success(mockPlaceEntry))
        composeTestRule.onNodeWithTag(searchDetailFavoriteButton).apply {
            assertExists()
            assertIsNotEnabled()
        }
    }
}