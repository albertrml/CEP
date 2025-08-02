package br.com.arml.cep.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
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
    private val mockOnBackPress: () -> Unit = mockk()
    private lateinit var displayAddressTitle: String
    private lateinit var mockPlaceEntry: PlaceEntry
    private lateinit var backButton: String

    private lateinit var searchDetailPaneHeader: String
    private lateinit var searchDetailPaneTitleHeader: String
    private lateinit var searchDetailPaneIconHeader: String
    private lateinit var searchDetailPaneOnSuccess: String
    private lateinit var searchDetailPaneOnLoading: String
    private lateinit var searchDetailPaneOnLoadingCircularProgressIndicator: String
    private lateinit var searchDetailPaneOnFailure: String

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        /*** Header ***/
        searchDetailPaneHeader = context.getString(
            R.string.testTag_searchScreen_detailPane_header)
        searchDetailPaneTitleHeader = context.getString(R.string.testTag_header_title)
        searchDetailPaneIconHeader = context.getString(R.string.testTag_header_icon)

        /*** On Loading ***/
        searchDetailPaneOnLoading = context.getString(
            R.string.testTag_searchScreen_detailPane_onLoading)
        searchDetailPaneOnLoadingCircularProgressIndicator = context.getString(
            R.string.testTag_searchScreen_detailPane_onLoading_circularProgressIndicator)

        /*** On Failure ***/
        searchDetailPaneOnFailure = context.getString(R.string.testTag_searchScreen_detailPane_onFailure)

        /*** On Success ***/
        searchDetailPaneOnSuccess = context.getString(R.string.testTag_searchScreen_detailPane_onSuccess)


        displayAddressTitle = context.getString(R.string.display_address_title)
        backButton = context.getString(R.string.icon_button_tag)
        mockPlaceEntry = mockPlaceEntries[0] // Use seu mockAddress
        every { mockOnBackPress() } answers {
            println("mockOnBackPress CALLED")
        }
    }

    private fun setDisplayScreenContent(response: Response<PlaceEntry>) {
        composeTestRule.setContent {
            SearchDetailPane(
                modifier = Modifier.fillMaxSize(),
                response = response,
                onBackPress = mockOnBackPress,
                onFavoriteClick = {}
            )
        }
    }

    @Test
    fun searchDetailPane_shouldShowIconAndTitleOnHeader() {
        setDisplayScreenContent(Response.Success(mockPlaceEntry))
        composeTestRule.apply{
            onNodeWithTag(searchDetailPaneHeader).assertExists()
            onNodeWithTag(searchDetailPaneTitleHeader).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneIconHeader).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneIconHeader).performClick()
            verify { mockOnBackPress() }
        }
    }

    @Test
    fun searchDetailPane_shouldShowCircularProgressIndicatorOnLoading(){
        setDisplayScreenContent(Response.Loading)
        composeTestRule.apply{
            onNodeWithTag(searchDetailPaneHeader).assertExists()
            onNodeWithTag(searchDetailPaneTitleHeader).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneIconHeader).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneOnLoading).assertExists()
            onNodeWithTag(searchDetailPaneOnSuccess).assertDoesNotExist()
            onNodeWithTag(searchDetailPaneOnFailure).assertDoesNotExist()
            onNodeWithTag(searchDetailPaneOnLoadingCircularProgressIndicator).assertExists()
        }
    }

    @Test
    fun searchDetailPane_shouldShowFailureMessageOnFailure(){
        val errorMsg = CepException.NotFoundCepException().message
        setDisplayScreenContent(Response.Failure(CepException.NotFoundCepException()))
        composeTestRule.apply{
            onNodeWithTag(searchDetailPaneHeader).assertExists()
            onNodeWithTag(searchDetailPaneTitleHeader).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneIconHeader).assertIsDisplayed()
            onNodeWithTag(searchDetailPaneOnFailure).assertExists()
            onNodeWithTag(searchDetailPaneOnLoading).assertDoesNotExist()
            onNodeWithTag(searchDetailPaneOnSuccess).assertDoesNotExist()
            onNodeWithText(errorMsg).assertIsDisplayed()
        }
    }

    @Test
    fun searchDetailPane_whenSuccess_showsAddressScreenWithCorrectData() {
        setDisplayScreenContent(Response.Success(mockPlaceEntry))
        val address = mockPlaceEntry.address
        composeTestRule.onNodeWithText(address.zipCode).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.street).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.complement).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.district).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.city).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.state).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.uf).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.region).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.country).assertIsDisplayed()
        composeTestRule.onNodeWithText(address.ddd).assertIsDisplayed()
    }

    @Test
    fun searchDetailPane_shouldShowNotFoundCepErrorMessage_whenFailureWithNotFoundCepException() {
        val errorMessage = CepException.NotFoundCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }

    @Test
    fun searchDetailPane_shouldShowInputCepExceptionMessage_whenFailureWithInputCepException() {
        val errorMessage = CepException.InputCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }

    @Test
    fun searchDetailPane_shouldShowSizeCepExceptionMessage_whenFailureWithSizeCepException() {
        val errorMessage = CepException.SizeCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }

    @Test
    fun searchDetailPane_shouldShowEmptyCepExceptionMessage_whenFailureWithEmptyCepException() {
        val errorMessage = CepException.EmptyCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }
}