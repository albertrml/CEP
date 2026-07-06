package br.com.arml.cep.ui.component.common.filter

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.toEndOfDay
import br.com.arml.cep.model.utils.toFormattedDate
import br.com.arml.cep.ui.screen.component.common.filter.PeriodFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class PeriodFilterTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    /*** DatePicker ***/
    private val confirmButtonDatePickerTag = ctx.getString(R.string.datePickerModal_confirmButton_testTag)

    /*** Filter Composable ***/
    private val periodFilterComposable = ctx.getString(R.string.periodFilter_composable_testTag)

    /*** End Date ***/
    private val periodFilterEndDateField = ctx.getString(R.string.periodFilter_endDateField_testTag)
    private val periodFilterEndDateFieldLabel = ctx.getString(R.string.periodFilter_endDateField_label)

    /*** Initial Date ***/
    private val periodFilterStartDateField = ctx.getString(R.string.periodFilter_startDateField_testTag)
    private val periodFilterStartDateFieldLabel = ctx.getString(R.string.periodFilter_startDateField_label)

    /*** Filter Button ***/
    private val periodFilterButton = ctx.getString(R.string.periodFilter_filterButton_testTag)
    private val mockOnFilterByPeriod: (Long, Long) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp() {
        composeTestRule.setContent {
            PeriodFilter(onFilterByRange = mockOnFilterByPeriod)
        }

        every { mockOnFilterByPeriod(any(),any()) } answers {
            println("onFilterByPeriod called with ${args[0]} and ${args[1]}")
        }
    }

    private fun mockDates(): Pair<Long, Long> {
        val zone = ZoneOffset.UTC
        val today = LocalDate.now(zone)

        val start = if(today.dayOfMonth == 14) 13 else 14
        val end = if(today.dayOfMonth == 16) 17 else 16

        val startDate = today
            .withDayOfMonth(start)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        val endDate = today
            .withDayOfMonth(end)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
            .toEndOfDay()

        return startDate to endDate
    }

    private fun nodeDateFormat(timeInMillis: Long): String{
        val formatter = DateTimeFormatter
            .ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())

        return Instant.ofEpochMilli(timeInMillis)
            .atZone(ZoneOffset.UTC)
            .format(formatter)
    }

    private fun selectDateInPicker(timeInMillis: Long) {
        val dateFormatted = nodeDateFormat(timeInMillis)

        composeTestRule.apply {
            onNodeWithText(
                text = dateFormatted,
                substring = true,
                ignoreCase = true,
                useUnmergedTree = true
            ).performClick()
            onNodeWithTag(confirmButtonDatePickerTag).performClick()
            waitForIdle()
        }
    }

    @Test
    fun periodFilter_shouldDisplayAPartirDeAndAteFieldsAndFilterButton_whenPeriodFilterIsCalled() {
        composeTestRule.onNodeWithTag(periodFilterComposable).assertExists()
        composeTestRule.onNodeWithTag(periodFilterStartDateField).assertExists()
        composeTestRule.onNodeWithTag(periodFilterEndDateField).assertExists()
    }

    @Test
    fun periodFilter_shouldDisplayStartDate_whenStartDatIsSelected_inStartDateField() {
        val date = mockDates().first
        val expectedDate = date.toFormattedDate()
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField)
                .assert(hasText(periodFilterStartDateFieldLabel))
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(date)
            onNodeWithTag(periodFilterStartDateField).assert(
                hasText(expectedDate)
            )
        }
    }

    @Test
    fun periodFilter_shouldDisplayEndDate_whenTodayIsSelected_inEndDateField() {
        val date = mockDates().second
        val expectedDate = date.toFormattedDate()
        composeTestRule.apply {
            onNodeWithTag(periodFilterEndDateField)
                .assert(hasText(periodFilterEndDateFieldLabel))
                .performClick()

            selectDateInPicker(date)

            onNodeWithTag(periodFilterEndDateField)
                .assert(hasText(expectedDate))
        }
    }

    @Test
    fun periodFilter_shouldUnableButton_whenNoDatesAreSelected() {
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterEndDateField)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun periodFilter_shouldUnableButton_whenOnlyInitialDateIsSelected() {
        val date = mockDates().first
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(date)
            onNodeWithTag(periodFilterEndDateField)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun periodFilter_shouldUnableButton_whenOnlyEndDateIsSelected() {
        val date = mockDates().second
        composeTestRule.apply {
            onNodeWithText(periodFilterStartDateFieldLabel)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(date)
            onNodeWithTag(periodFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun periodFilter_shouldActiveButton_whenBothDatesAreSelected() {
        val (startDate, endDate) = mockDates()
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(startDate)
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(endDate)
            onNodeWithTag(periodFilterButton).assertIsEnabled()
        }
    }

    @Test
    fun periodFilter_shouldFilter_whenFilterButtonIsActiveAndClicked() {
        val (startDate, endDate) = mockDates()
        composeTestRule.apply{
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(startDate)
            waitForIdle()
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(endDate)
            waitForIdle()
            onNodeWithTag(periodFilterButton).assertIsEnabled().performClick()
            verify { mockOnFilterByPeriod(startDate,endDate) }
        }
    }
}