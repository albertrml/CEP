package br.com.arml.cep.ui.cache

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.exception.UnknownException
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.cache.CacheListPaneComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheListPaneComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val headerTag = ctx.getString(R.string.cacheListPaneComponent_header_testTag)
    private val onSuccessTag = ctx.getString(R.string.cacheListPaneOnSuccess_component_testTag)
    private val onLoadingTag = ctx.getString(R.string.cacheListPaneOnLoading_component_testTag)
    private val onFailureTag = ctx.getString(R.string.cacheListPaneOnFailure_component_testTag)

    @Test
    fun cacheListPaneComponent_shouldDisplaySuccessState_whenResponseIsSuccess() {
        composeTestRule.setContent {
            CacheListPaneComponent(
                fetchResponse = Response.Success(mockUnfavoritePlaces),
                onDeletePlace = {},
                onCepFilter = {},
                onClearFilter = {},
                onNavigateToDetail = {},
                onDeleteAllCache = {}
            )
        }

        composeTestRule.onNodeWithTag(headerTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(onSuccessTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(onLoadingTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(onFailureTag).assertDoesNotExist()
    }

    /*@Test
    fun cacheListPaneComponent_shouldDisplayLoadingState_whenResponseIsLoading() {
        composeTestRule.setContent {
            CacheListPaneComponent(
                fetchResponse = Response.Loading,
                onDeletePlace = {},
                onCepFilter = {},
                onClearFilter = {},
                onNavigateToDetail = {},
                onDeleteAllCache = {}
            )
        }

        composeTestRule.onNodeWithTag(headerTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(onLoadingTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(onSuccessTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(onFailureTag).assertDoesNotExist()
    }*/

    @Test
    fun cacheListPaneComponent_shouldDisplayFailureState_whenResponseIsFailure() {
        val exception = UnknownException.FetchPlaceException()
        val failureResponse = Response.Failure(exception)

        composeTestRule.setContent {
            CacheListPaneComponent(
                fetchResponse = failureResponse,
                onDeletePlace = {},
                onCepFilter = {},
                onClearFilter = {},
                onNavigateToDetail = {},
                onDeleteAllCache = {}
            )
        }

        composeTestRule.onNodeWithTag(headerTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(onFailureTag).assertIsDisplayed()
        composeTestRule.onNodeWithText(exception.message).assertIsDisplayed()
        composeTestRule.onNodeWithTag(onLoadingTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(onSuccessTag).assertDoesNotExist()
    }
}