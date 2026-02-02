package br.com.arml.cep.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockPlaces
import br.com.arml.cep.ui.screen.component.search.SearchDetailPane
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class SearchDetailPaneTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val headerTag = ctx.getString(R.string.header_component_testTag)
    private val saveButtonTag =
        ctx.getString(R.string.searchDetailPaneOnSuccess_saveButton_testTag)
    private val goBackButtonTag = ctx.getString(R.string.headerContent_iconButton_testTag)
    private val addressFormTag = ctx.getString(R.string.addressForms_component_testTag)
    private val circularProgressIndicatorTag = ctx.getString(
        R.string.searchDetailPaneOnLoading_component_testTag
    )

    private val loadingTag = ctx.getString(R.string.searchDetailPaneOnLoading_loading_testTag)
    private val successTag = ctx.getString(R.string.searchDetailPaneOnSuccess_component_testTag)
    private val failureTag = ctx.getString(R.string.searchDetailPaneOnFailure_component_testTag)

    private val mockOnBackPress: () -> Unit = mockk(relaxed = true)
    private val mockOnFavorite: (Place) -> Unit = mockk(relaxed = true)

    @Test
    fun searchDetailPaneTest_shouldLoadingComponents_whenResponseIsLoading(){
        composeTestRule.apply{
            setContent {
                SearchDetailPane(
                    response = Response.Loading,
                    onBackPress = mockOnBackPress,
                    onFavoriteClick = mockOnFavorite
                )
            }
            onNodeWithTag(loadingTag).assertExists()
            onNodeWithTag(successTag).assertDoesNotExist()
            onNodeWithTag(failureTag).assertDoesNotExist()

            onNodeWithTag(headerTag).assertExists()
            onNodeWithTag(circularProgressIndicatorTag).assertExists()
        }
    }

    @Test
    fun searchDetailPaneTest_shouldFailuresComponents_whenResponseIsFailure(){
        val exception = CepException.NotFoundCepException()
        val expectedMsg = exception.message
        composeTestRule.apply{
            setContent {
                SearchDetailPane(
                    response = Response.Failure(exception),
                    onBackPress = mockOnBackPress,
                    onFavoriteClick = mockOnFavorite
                )
            }
            onNodeWithTag(loadingTag).assertDoesNotExist()
            onNodeWithTag(successTag).assertDoesNotExist()
            onNodeWithTag(failureTag).assertExists()

            onNodeWithTag(headerTag).assertExists()
            onNodeWithText(expectedMsg).assertExists()
        }
    }

    @Test
    fun searchDetailPaneTest_shouldSuccessComponents_whenResponseIsSuccess() {
        composeTestRule.apply {
            setContent {
                SearchDetailPane(
                    response = Response.Success(mockPlaces(1, false).first()),
                    onBackPress = mockOnBackPress,
                    onFavoriteClick = mockOnFavorite
                )
            }
            onNodeWithTag(loadingTag).assertDoesNotExist()
            onNodeWithTag(successTag).assertExists()
            onNodeWithTag(failureTag).assertDoesNotExist()

            onNodeWithTag(headerTag).assertExists()
            onNodeWithTag(addressFormTag).assertExists()
        }
    }

    @Test
    fun searchDetailPaneTest_shouldInvokesOnBackPressCallback_whenBackButtonIsClickedWhateverResponseIs() {
        val responses = listOf(
            Response.Loading,
            Response.Success(mockPlaces(1, false).first()),
            Response.Failure(Exception())
        )

        var response by mutableStateOf<Response<Place>>(Response.Loading)

        composeTestRule.apply {
            setContent {
                SearchDetailPane(
                    response = response,
                    onBackPress = mockOnBackPress,
                    onFavoriteClick = mockOnFavorite
                )
            }
            onNodeWithTag(goBackButtonTag).printToLog("searchDetailPane")
            responses.forEach {
                Log.d("searchDetailPane", "response: $it")
                response = it
                onNodeWithTag(goBackButtonTag)
                    .assertExists()
                    .performClick()
            }
            verify(exactly = 3) { mockOnBackPress() }
        }
    }

    @Test
    fun searchDetailPaneTest_shouldUnableSaveButton_whenPlaceIsFavorite(){
        val place = mockPlaces(1, true).first()
        composeTestRule.apply {
            setContent {
                SearchDetailPane(
                    response = Response.Success(place),
                    onBackPress = mockOnBackPress,
                    onFavoriteClick = mockOnFavorite
                )
            }
            onNodeWithTag(saveButtonTag)
                .assertExists()
                .assertIsNotEnabled()
        }
    }

    @Test
    fun searchDetailPaneTest_shouldInvokesOnFavoriteClick_whenPlaceIsUnfavoriteAndSaveButtonIsClicked(){
        val place = mockPlaces(1, false).first()
        composeTestRule.apply {
            setContent {
                SearchDetailPane(
                    response = Response.Success(place),
                    onBackPress = mockOnBackPress,
                    onFavoriteClick = mockOnFavorite
                )
            }
            onNodeWithTag(saveButtonTag).assertExists().performClick()
            verify(exactly = 1) { mockOnFavorite(place) }
        }
    }
}