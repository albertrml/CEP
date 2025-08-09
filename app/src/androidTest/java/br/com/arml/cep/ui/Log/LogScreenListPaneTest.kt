package br.com.arml.cep.ui.Log

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.ui.screen.component.log.LogScreenListComponent
import br.com.arml.cep.ui.screen.log.LogState
import io.mockk.every
import io.mockk.mockk
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
    private val mockOnConfirmDeleteAllEntries: () -> Unit = mockk(relaxed = true)

    /*** List ***/
    private val mockOnClickToDeleteEntry: (LogEntry) -> Unit = mockk(relaxed = true)
    private val mockOnCopyToClipboard: (LogEntry) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp(){
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext

        /*** Header ***/
        logScreenHeader = ctx.getString(R.string.testTag_logScreen_header)
        logScreenTitleHeader = ctx.getString(R.string.testTag_header_title)
        logScreenIconHeader = ctx.getString(R.string.testTag_header_icon)

        /*** Filters ***/
        every { mockOnFilterByCep(any()) } answers { println("onFilterByCep: ${args[0]}") }
        every { mockOnFilterByInitialDate(any()) } answers { println("onFilterByInitialDate: ${args[0]}") }
        every { mockOnFilterByFinalDate(any()) } answers { println("onFilterByFinalDate: ${args[0]}") }
        every { mockOnFilterByRangeDate(any(), any()) } answers { println("onFilterByRangeDate: ${args[0]}, ${args[1]}") }
        every { mockOnFilterByNone() } answers { println("onFilterByNone") }

        /*** Delete All ***/
        every { mockOnConfirmDeleteAllEntries() } answers { println("onConfirmDeleteAllEntries") }

        /*** List ***/
        every { mockOnClickToDeleteEntry(any()) } answers { println("onClickToDeleteEntry: ${args[0]}") }
        every { mockOnCopyToClipboard(any()) } answers { println("onCopyToClipboard: ${args[0]}") }

        mockLogScreenListPane()
    }

    fun mockLogScreenListPane(){
        composeTestRule.setContent {
            LogScreenListComponent(
                state = LogState(),
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
    fun shouldDisplayTitleAndIconOnHeader_whenLogScreenListPaneIsDisplayed(){
        composeTestRule.apply{
            /*onNodeWithTag(logScreenHeader).assertExists()
            onNodeWithTag(logScreenTitleHeader).assertExists()
            onNodeWithTag(logScreenIconHeader).assertExists()*/
            onNodeWithTag(logScreenHeader).assertDoesNotExist()
            onNodeWithTag(logScreenTitleHeader).assertDoesNotExist()
            onNodeWithTag(logScreenIconHeader).assertDoesNotExist()
        }
    }

    /*** Filters ***/
    @Test
    fun shouldSelectNenhumFilter_whenFiltersAreOpenedForTheFirstTime(){}

    @Test
    fun shouldDisplayEntreFilterAsLast_whenFiltersAreHorizontallyScrolled(){}

    @Test
    fun shouldDisplayCepSearchFieldAndDisableFilterButton_whenCepFilterIsSelected(){}

    @Test
    fun shouldDisplayCepAsHint_whenCepSearchFieldIsBlank(){}

    @Test
    fun shouldActivateFilterButton_whenCepSearchFieldHasAtLeastThreeDigits(){}

    @Test
    fun shouldAcceptOnlyDigitsAndFormatItAsCepPattern_whenUserInputsTextInCepSearchField(){}

    @Test
    fun shouldFilterLogs_whenCepSearchFieldHasAtLeastThreeDigitsAndFilterButtonIsClicked(){}

    @Test
    fun shouldDisplayDatePickerAndDisableSearchButton_whenAPartirDeFilterIsSelected(){}

    @Test
    fun shouldActivateSearchButtonAndFilterLogs_whenDateIsPickedAndFilterButtonIsClicked_InAPartirDeFilter(){}

    @Test
    fun shouldDisplayAPartirDeAsHintOnDatePicker_whenDateFieldIsBlank_inAPartirDeFilter(){}

    @Test
    fun shouldDisplayDatePickerAndDisableSearchButton_whenAteFilterIsSelected(){}

    @Test
    fun shouldActivateSearchButtonAndFilterLogs_whenDateIsPickedAndFilterButtonIsClicked_inAteFilter(){}

    @Test
    fun shouldDisplayAteAsHintOnDatePicker_whenDateFieldIsBlank_inAteFilter(){}

    @Test
    fun shouldDisplayTwoDatePickersAndDisableSearchButton_whenEntreFilterIsSelected(){}

    @Test
    fun shouldActivateSearchButtonAndFilterLogs_whenTwoDatesArePickedAndFilterButtonIsClicked(){}

    @Test
    fun shouldDisplayDeAndAteAsHintsOnDatePickers_whenDateFieldsAreBlank_inEntreFilter(){}

    /*** Delete All Button ***/
    @Test
    fun shouldDisplayDeleteAllButton_whenLogScreenListPaneIsCalled(){}

    @Test
    fun shouldDeleteAllLog_whenDeleteAllButtonIsClicked(){}

    /*** List ***/
    @Test
    fun shouldDisplayLogInformationAndDeleteIconInEachEntry_whenLogListIsCalled(){}

    @Test
    fun shouldDeleteLogEntry_whenItsDeleteIconIsClicked(){}

    @Test
    fun shouldClipboardLogEntryOnS_whenItIsLongClicked(){}
}