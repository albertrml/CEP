package br.com.arml.cep.ui.component.log.listpane.item

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.utils.toFormattedUTC
import br.com.arml.cep.model.utils.toFormattedUTCDate
import br.com.arml.cep.ui.screen.component.log.listpane.item.LogList
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val mockLogs = mockLogEntries.groupBy { it.timestamp.toFormattedUTCDate() }
    private val mockOnClickToDelete: (Log) -> Unit = mockk(relaxed = true)
    private val mockOnCopyToClipboard: (Log) -> Unit = mockk(relaxed = true)

    @Test
    fun logList_shouldDisplayHeadersAndItems() {
        val scrollButton = ctx.getString(R.string.fastScrollList_downButton_text)
        composeTestRule.apply {
            setContent {
                LogList(
                    logEntries = mockLogs,
                    onClickToDelete = mockOnClickToDelete,
                    onCopyToClipboard = mockOnCopyToClipboard
                )
            }
            onRoot().printToLog("LogListTest")
            mockLogs.forEach { (date, logs) ->
                // Verify header is displayed
                val nodeHeader = onNodeWithText(date)
                if(nodeHeader.isNotDisplayed())
                    onNodeWithText(scrollButton, useUnmergedTree = true).performClick()
                nodeHeader.assertIsDisplayed()

                // Verify items for that date are displayed
                logs.forEach { log ->
                    val logElementDescription = ctx.getString(
                        R.string.logElement_component_testTag,
                        log.toString()
                    )
                    val nodeElement = onNodeWithTag(logElementDescription)
                    if (nodeElement.isNotDisplayed())
                        onNodeWithText(scrollButton, useUnmergedTree = true).performClick()
                    nodeElement.assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun logList_shouldInvokeOnClickToDelete_whenDeleteIsClicked() {
        val logToDelete = mockLogEntries.first()
        val deleteButtonDescription = ctx.getString(
            R.string.logElement_deleteButton_description,
            logToDelete.cep.text,
            logToDelete.timestamp.toFormattedUTC()
        )

        composeTestRule.setContent {
            LogList(
                logEntries = mockLogs,
                onClickToDelete = mockOnClickToDelete,
                onCopyToClipboard = mockOnCopyToClipboard
            )
        }

        composeTestRule.onNodeWithContentDescription(deleteButtonDescription).performClick()

        verify { mockOnClickToDelete(logToDelete) }
    }

    @Test
    fun logList_shouldInvokeOnCopyToClipboard_whenItemIsClicked() {
        val logToCopy = mockLogEntries.first()

        // The clickable area of the LogElement has a combined content description
        val clickableElementDescription = ctx.getString(
            R.string.logElement_component_testTag,
            logToCopy.toString()
        )

        composeTestRule.setContent {
            LogList(
                logEntries = mockLogs,
                onClickToDelete = mockOnClickToDelete,
                onCopyToClipboard = mockOnCopyToClipboard
            )
        }

        composeTestRule.onNodeWithTag(clickableElementDescription).performClick()

        verify { mockOnCopyToClipboard(logToCopy) }
    }
}