package br.com.arml.cep.ui.Log

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.ui.screen.component.log.LogListComponent
import br.com.arml.cep.ui.screen.log.LogState
import br.com.arml.cep.ui.utils.LogFilterOption
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LogScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    /** States **/
    private val state: LogState = LogState()
    private val loadingFetchEntries: Response<List<LogEntry>> = Response.Loading
    private val successFetchEntries: Response<List<LogEntry>> = Response.Success(mockLogEntries)
    private val failureFetchEntries: Response<List<LogEntry>> = Response.Failure(Exception("Test Failed"))
    private val initialDate: Long = mockLogEntries[3].timestamp.time
    private val finalDate: Long = mockLogEntries[7].timestamp.time
    private val cep: String = mockLogEntries[5].cep.text
    private val clippedLog: LogEntry = mockLogEntries[5]

    /** Events **/
    private val mockOnFilterByCep: (String) -> Unit = mockk()
    private val mockOnFilterByInitialDate: (Long) -> Unit= mockk()
    private val mockOnFilterByFinalDate: (Long) -> Unit = mockk()
    private val mockOnFilterByRangeDate: (Long, Long) -> Unit = mockk()
    private val mockOnFilterByNone: () -> Unit = mockk()
    private val mockOnClickToDeleteEntry: (LogEntry) -> Unit = mockk()
    private val mockOnConfirmDeleteAllEntries: () -> Unit = mockk()
    private val mockOnCopyToClipboard: (LogEntry) -> Unit = mockk()

    /** UI Components **/
    private lateinit var header: String
    private lateinit var iconHeader: String
    private lateinit var titleHeader: String
    private lateinit var filterComponent: String
    private lateinit var deleteAllComponent: String
    private lateinit var logListOnSuccess: String
    private lateinit var logListOnLoading: String
    private lateinit var logListOnFailure: String
    private lateinit var deleteAllLogAlert: String

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().targetContext.apply {
            header = getString(R.string.testTag_logScreen_header)
            iconHeader = getString(R.string.testTag_header_icon)
            titleHeader = getString(R.string.testTag_header_title)
            filterComponent = getString(R.string.testTag_logScreen_filterComponent)
            deleteAllComponent = getString(R.string.testTag_logScreen_filter_deleteAllComponent)
            logListOnSuccess = getString(R.string.testTag_logScreen_fetching_OnSuccess)
            logListOnLoading = getString(R.string.testTag_logScreen_fetching_OnLoading)
            logListOnFailure = getString(R.string.testTag_logScreen_fetching_OnFailure)
            deleteAllLogAlert = getString(R.string.testTag_logScreen_DeleteAllLogAlert)
        }

        every { mockOnFilterByCep(any()) } answers { println("mockOnFilterByCep CALLED") }
        every { mockOnFilterByInitialDate(any()) } answers { println("mockOnFilterByInitialDate CALLED") }
        every { mockOnFilterByFinalDate(any()) } answers { println("mockOnFilterByFinalDate CALLED") }
        every { mockOnFilterByRangeDate(any(), any()) } answers { println("mockOnFilterByRangeDate CALLED") }
        every { mockOnFilterByNone() } answers { println("mockOnFilterByNone CALLED") }
        every { mockOnClickToDeleteEntry(any()) } answers { println("mockOnClickToDeleteEntry CALLED") }
        every { mockOnConfirmDeleteAllEntries() } answers { println("mockOnConfirmDeleteAllEntries CALLED") }
        every { mockOnCopyToClipboard(any()) } answers { println("mockOnCopyToClipboard CALLED") }
    }

    private fun launchLogListComponent(){
        composeTestRule.setContent {
            LogListComponent(
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

    @Test
    fun logScreen_shouldDisplayTitleAndIconOnHeader(){
        launchLogListComponent()
        composeTestRule.apply{
            onNodeWithTag(header).assertExists()
            onNodeWithTag(iconHeader).assertExists()
            onNodeWithTag(titleHeader).assertExists()
        }
    }

    @Test
    fun logScreen_shouldPerformCepFilter(){
        launchLogListComponent()
        composeTestRule.apply{
            onNodeWithTag(filterComponent).assertExists()
            onNodeWithText(LogFilterOption.ByCep.name).performClick()
        }

    }
}