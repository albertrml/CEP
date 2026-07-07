package br.com.arml.cep.ui.log

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.core.response.Response
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.ui.screen.component.log.LogListPaneComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogListPaneComponentTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val headerTag = ctx.getString(R.string.logListPaneComponent_header_testTag)
    private val filterTag =  ctx.getString(R.string.logFilterComponent_component_testTag)
    private val logListPaneOnSuccessTag = ctx.getString(R.string.logListPaneOnSuccess_component_testTag)
    private val logListPaneOnLoadingTag = ctx.getString(R.string.logListPaneOnLoading_component_testTag)
    private val logListPaneOnFailureTag = ctx.getString(R.string.logListPaneOnFailure_component_testTag)


    @Test
    fun logListPaneComponent_shouldDisplaySuccessComponents_whenEntriesIsSuccess(){
        val onSuccess = Response.Success(mockLogEntries)
        composeTestRule.apply {
            setContent {
                LogListPaneComponent(
                    entries = onSuccess,
                    onFilterByCep = {},
                    onFilterByInitialDate = {},
                    onFilterByFinalDate = {},
                    onFilterByRangeDate = { _, _ -> },
                    onFilterByNone = {},
                    onClickToDelete = {},
                    onClickToDeleteAll = {},
                    onCopyToClipboard = {}
                )
            }
            onNodeWithTag(headerTag).assertIsDisplayed()
            onNodeWithTag(filterTag).assertIsDisplayed()
            onNodeWithTag(logListPaneOnSuccessTag).assertIsDisplayed()
            onNodeWithTag(logListPaneOnLoadingTag).assertIsNotDisplayed()
            onNodeWithTag(logListPaneOnFailureTag).assertIsNotDisplayed()
        }
    }

    @Test
    fun logListPaneComponent_shouldDisplayFailureComponents_whenEntriesIsFailure(){
        val exception = Exception()
        val onFailure = Response.Failure(exception)
        composeTestRule.apply {
            setContent {
                LogListPaneComponent(
                    entries = onFailure,
                    onFilterByCep = {},
                    onFilterByInitialDate = {},
                    onFilterByFinalDate = {},
                    onFilterByRangeDate = { _, _ -> },
                    onFilterByNone = {},
                    onClickToDelete = {},
                    onClickToDeleteAll = {},
                    onCopyToClipboard = {}
                )
            }
            onNodeWithTag(headerTag).assertIsDisplayed()
            onNodeWithTag(filterTag).assertIsDisplayed()
            onNodeWithTag(logListPaneOnSuccessTag).assertIsNotDisplayed()
            onNodeWithTag(logListPaneOnLoadingTag).assertIsNotDisplayed()
            onNodeWithTag(logListPaneOnFailureTag).assertIsDisplayed()
        }
    }
}