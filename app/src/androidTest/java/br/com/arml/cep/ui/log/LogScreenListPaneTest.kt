package br.com.arml.cep.ui.log

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.utils.toFormattedUTC
import br.com.arml.cep.ui.screen.component.log.LogScreenListComponent
import br.com.arml.cep.ui.screen.log.LogState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LogScreenListPaneTest {
        @get:Rule
        val composeTestRule = createComposeRule()

        /*** Header ***/
        private lateinit var logScreenHeader: String
        private lateinit var logScreenTitleHeader: String
        private lateinit var logScreenIconHeader: String

        /*** Filters ***/
        private lateinit var logScreenFilterComponent: String
        private val mockOnFilterByCep: (String) -> Unit = mockk(relaxed = true)
        private val mockOnFilterByInitialDate: (Long) -> Unit = mockk(relaxed = true)
        private val mockOnFilterByFinalDate: (Long) -> Unit = mockk(relaxed = true)
        private val mockOnFilterByRangeDate: (Long, Long) -> Unit = mockk(relaxed = true)
        private val mockOnFilterByNone: () -> Unit = mockk(relaxed = true)

        /*** Delete All ***/
        private lateinit var logScreenDeleteAllButton: String
        private lateinit var logScreenDeleteAllAlertDialog: String
        private lateinit var logScreenDeleteAllDialogAlertTitle: String
        private lateinit var logScreenDeleteAllDialogAlertMessage: String
        private lateinit var logScreenDeleteAllDialogAlertConfirmButton: String
        private lateinit var logScreenDeleteAllDialogAlertDismissButton: String
        private val mockOnConfirmDeleteAllEntries: () -> Unit = mockk(relaxed = true)

        /*** List ***/
        private lateinit var logScreenListIsSuccess: String
        private lateinit var logScreenListIsLoading: String
        private lateinit var logScreenListIsFailure: String
        private val mockOnClickToDeleteEntry: (Log) -> Unit = mockk(relaxed = true)
        private val mockOnCopyToClipboard: (Log) -> Unit = mockk(relaxed = true)

        @Before
        fun setUp(){
            val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext

            /*** Header ***/
            logScreenHeader = ctx.getString(R.string.testTag_logScreen_header)
            logScreenTitleHeader = ctx.getString(R.string.testTag_header_title)
            logScreenIconHeader = ctx.getString(R.string.testTag_header_icon)

            /*** Filters ***/
            logScreenFilterComponent = ctx.getString(R.string.testTag_logScreen_filterComponent)
            every { mockOnFilterByCep(any()) } answers { println("onFilterByCep: ${args[0]}") }
            every { mockOnFilterByInitialDate(any()) } answers { println("onFilterByInitialDate: ${args[0]}") }
            every { mockOnFilterByFinalDate(any()) } answers { println("onFilterByFinalDate: ${args[0]}") }
            every { mockOnFilterByRangeDate(any(), any()) } answers { println("onFilterByRangeDate: ${args[0]}, ${args[1]}") }
            every { mockOnFilterByNone() } answers { println("onFilterByNone") }

            /*** Delete All ***/
            logScreenDeleteAllButton = ctx.getString(R.string.testTag_logScreen_filter_deleteAllComponent)
            logScreenDeleteAllAlertDialog = ctx.getString(R.string.testTag_logScreen_DeleteAllLogAlert)
            logScreenDeleteAllDialogAlertTitle = ctx.getString(R.string.log_delete_all_log_title)
            logScreenDeleteAllDialogAlertMessage = ctx.getString(R.string.log_delete_all_log_alert)
            logScreenDeleteAllDialogAlertConfirmButton = ctx.getString(R.string.alert_dialog_confirm_button)
            logScreenDeleteAllDialogAlertDismissButton = ctx.getString(R.string.alert_dialog_dismiss_button)

            every { mockOnConfirmDeleteAllEntries() } answers { println("onConfirmDeleteAllEntries") }

            /*** List ***/
            logScreenListIsSuccess = ctx.getString(R.string.testTag_logScreen_fetching_OnSuccess)
            logScreenListIsLoading = ctx.getString(R.string.testTag_logScreen_fetching_OnLoading)
            logScreenListIsFailure = ctx.getString(R.string.testTag_logScreen_fetching_OnFailure)
            every { mockOnClickToDeleteEntry(any()) } answers { println("onClickToDeleteEntry: ${args[0]}") }
            every { mockOnCopyToClipboard(any()) } answers { println("onCopyToClipboard: ${args[0]}") }
        }

        fun mockLogScreenListPane(state: LogState = LogState()){
            composeTestRule.setContent {
                LogScreenListComponent(
                    state = state,
                    onFilterByCep = mockOnFilterByCep,
                    onFilterByInitialDate = mockOnFilterByInitialDate,
                    onFilterByFinalDate = mockOnFilterByFinalDate,
                    onFilterByRangeDate = mockOnFilterByRangeDate,
                    onFilterByNone = mockOnFilterByNone,
                    onClickToDeleteEntry = mockOnClickToDeleteEntry,
                    onConfirmDeleteAllEntries = mockOnConfirmDeleteAllEntries,
                    onCopyToClipboard = mockOnCopyToClipboard
                )
            }
        }

        /*** Header ***/
        @Test
        fun shouldDisplayHeaderComponent_whenLogScreenListPaneIsDisplayed(){
            mockLogScreenListPane()
            composeTestRule.apply{
                onNodeWithTag(logScreenHeader).assertExists()
                onNodeWithTag(logScreenTitleHeader).assertExists()
                onNodeWithTag(logScreenIconHeader).assertExists()
            }
        }

        /*** Filters ***/
        @Test
        fun shouldDisplayFilterComponent_whenLogScreenListPaneIsDisplayed(){
            mockLogScreenListPane()
            composeTestRule.apply {
                onNodeWithTag(logScreenFilterComponent).assertExists()
            }
        }

        /*** Delete All ***/
        @Test
        fun shouldDisplayDeleteAllComponent_whenLogScreenListPaneIsCalled(){
            mockLogScreenListPane()
            composeTestRule.onNodeWithTag(logScreenDeleteAllButton).apply {
                assertExists()
                assertIsEnabled()
            }
        }

        /*** List ***/
        @Test
        fun shouldDisplayLoadingIndicator_whenFetchLogEntriesIsLoading(){
            mockLogScreenListPane()
            composeTestRule.onNodeWithTag(logScreenListIsLoading).apply {
                assertExists()
                assertIsDisplayed()
            }
            composeTestRule.apply {
                onNodeWithTag(logScreenListIsSuccess).assertDoesNotExist()
                onNodeWithTag(logScreenListIsFailure).assertDoesNotExist()
            }
        }

        @Test
        fun shouldDisplayErrorIndicator_whenFetchLogEntriesIsFailure(){
            mockLogScreenListPane(
                state = LogState(
                    fetchEntries = Response.Failure(Exception())
                )
            )
            composeTestRule.onNodeWithTag(logScreenListIsFailure).apply {
                assertExists()
                assertIsDisplayed()
            }
            composeTestRule.apply {
                onNodeWithTag(logScreenListIsSuccess).assertDoesNotExist()
                onNodeWithTag(logScreenListIsLoading).assertDoesNotExist()
            }
        }

        @Test
        fun shouldDisplayLogList_whenFetchLogEntriesIsSuccess(){
            mockLogScreenListPane(
                state = LogState(
                    fetchEntries = Response.Success(mockLogEntries)
                )
            )
            composeTestRule.onNodeWithTag(logScreenListIsSuccess).apply {
                assertExists()
                assertIsDisplayed()
            }
            composeTestRule.apply {
                onNodeWithTag(logScreenListIsLoading).assertDoesNotExist()
                onNodeWithTag(logScreenListIsFailure).assertDoesNotExist()
            }
        }

        @Test
        fun shouldDisplayClipboardMessage_whenAnEntryIsCopiedToClipboard(){
            mockLogScreenListPane(
                state = LogState(
                    fetchEntries = Response.Success(mockLogEntries)
                )
            )
            composeTestRule.apply {
                val firstEntry = mockLogEntries.first().timestamp.toFormattedUTC()
                onNodeWithText(firstEntry, substring = true).performClick()
                verify { mockOnCopyToClipboard(mockLogEntries[0]) }
            }
        }

        @Test
        fun shouldDeleteAnEntry_whenDeleteBottomIsClicked(){
            mockLogScreenListPane(
                state = LogState(
                    fetchEntries = Response.Success(mockLogEntries)
                )
            )
            composeTestRule.apply {
                val firstEntry = mockLogEntries.first().timestamp.toFormattedUTC()
                onNodeWithText(firstEntry, substring = true).onChild().performClick()
                verify { mockOnClickToDeleteEntry(mockLogEntries[0]) }
            }
        }
}