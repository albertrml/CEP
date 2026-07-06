package br.com.arml.cep.ui.component.log.listpane

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.utils.toFormattedUTC
import br.com.arml.cep.ui.screen.component.log.listpane.LogListPaneOnSuccess
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogListPaneOnSuccessTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val logListPaneOnSuccessTag = ctx.getString(
        R.string.logListPaneOnSuccess_component_testTag
    )
    private val deleteAllComponentTag = ctx.getString(
        R.string.deleteAllComponent_component_testTag
    )
    private val logListTag = ctx.getString(
        R.string.logList_component_testTag
    )

    @Test
    fun logListPaneOnSuccess_shouldDisplayDeleteAllComponentAndLogList(){
        composeTestRule.apply {
            setContent {
                LogListPaneOnSuccess(
                    logList = mockLogEntries,
                    onClickToDelete = {},
                    onClickToDeleteAll = {},
                    onCopyToClipboard = {}
                )
            }
            onNodeWithTag(logListPaneOnSuccessTag).assertExists()
            onNodeWithTag(deleteAllComponentTag).assertExists()
            onNodeWithTag(logListTag).assertExists()
        }
    }

    @Test
    fun logListPaneOnSuccess_shouldInvokesOnClickDeleteAllCallback_whenDeleteAllButtonIsClicked(){
        val deleteAllButton = ctx.getString(R.string.deleteAllComponent_button_testTag)
        val mockOnClickToDeleteAll: () -> Unit = mockk(relaxed = true)
        val confirmButton = ctx.getString(R.string.cepAlertdialog_confirmButton_text)
        composeTestRule.apply{
            setContent {
                LogListPaneOnSuccess(
                    logList = mockLogEntries,
                    onClickToDelete = {},
                    onClickToDeleteAll = mockOnClickToDeleteAll,
                    onCopyToClipboard = {}
                )
            }
            onNodeWithTag(deleteAllButton,useUnmergedTree = true).performClick()
            waitForIdle()
            onNodeWithText(confirmButton).performClick()
            verify(exactly = 1) { mockOnClickToDeleteAll() }
        }
    }

    @Test
    fun logListPaneOnSuccess_shouldInvokesOnClickToDeleteCallback_whenDeleteButtonIsClicked(){
        val mockOnClickToDelete: (Log) -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                LogListPaneOnSuccess(
                    logList = mockLogEntries,
                    onClickToDelete = mockOnClickToDelete,
                    onClickToDeleteAll = {},
                    onCopyToClipboard = {}
                )
            }
        }
        val expectedLog = mockLogEntries.first()
        val deleteButtonDescription = ctx.getString(
            R.string.logElement_deleteButton_description,
            expectedLog.cep.text,
            expectedLog.timestamp.toFormattedUTC()
        )
        composeTestRule.onNodeWithContentDescription(deleteButtonDescription).performClick()
        verify(exactly = 1) { mockOnClickToDelete(expectedLog) }
    }

    @Test
    fun logListPaneOnSuccess_shouldInvokesOnClickToDetailCallback_whenLogElementIsClicked(){
        val mockOnClickToDetail: (Log) -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                LogListPaneOnSuccess(
                    logList = mockLogEntries,
                    onClickToDelete = {},
                    onClickToDeleteAll = {},
                    onCopyToClipboard = mockOnClickToDetail
                )
            }
        }
        val expectedLog = mockLogEntries.first()
        val logElementTag = ctx.getString(
            R.string.logElement_component_testTag,
            expectedLog.toString()
        )
        composeTestRule.onNodeWithTag(logElementTag).performClick()
        verify(exactly = 1) { mockOnClickToDetail(expectedLog) }
    }
}