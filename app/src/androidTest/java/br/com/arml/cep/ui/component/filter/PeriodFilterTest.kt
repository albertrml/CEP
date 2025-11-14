package br.com.arml.cep.ui.component.filter

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
import br.com.arml.cep.model.utils.toFormattedBR
import br.com.arml.cep.ui.screen.component.common.PeriodFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class PeriodFilterTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    /*** DatePicker ***/
    private lateinit var datePickerModal: String
    private lateinit var confirmTextButton: String
    private lateinit var initialDateLabel: String
    private lateinit var finalDateLabel: String

    /*** Filter Composable ***/
    private lateinit var periodFilterComposable: String
    private val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())

    /*** End Date ***/
    private lateinit var periodFilterEndDateField: String
    private val currentTimeMillis = System.currentTimeMillis()
    private val currentDate = currentTimeMillis.toFormattedBR()
    private val todayFormatted = Instant.ofEpochMilli(currentTimeMillis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    /*** Initial Date ***/
    private lateinit var periodFilterStartDateField: String
    private val yesterdayTimestamp = currentTimeMillis - 24 * 60 * 60 * 1000
    private val yesterdayDate = yesterdayTimestamp.toFormattedBR()
    private val yesterdayFormatted = Instant.ofEpochMilli(yesterdayTimestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    /*** Filter Button ***/
    private lateinit var periodFilterButton: String
    private val mockOnFilterByPeriod: (Long, Long) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            periodFilterComposable = getString(R.string.testTag_periodFilter_composable)
            periodFilterStartDateField = getString(R.string.testTag_periodFilter_startDateField)
            periodFilterEndDateField = getString(R.string.testTag_periodFilter_endDateField)
            periodFilterButton = getString(R.string.testTag_periodFilter_button)

            datePickerModal = getString(R.string.testTag_datePickerModal)
            confirmTextButton = getString(R.string.testTag_datePicker_confirmTextButton)

            initialDateLabel = getString(R.string.log_filter_initial_date_label)
            finalDateLabel = getString(R.string.log_filter_final_date_label)
        }

        composeTestRule.setContent {
            PeriodFilter(onFilterByInitialDate = mockOnFilterByPeriod)
        }

        every { mockOnFilterByPeriod(any(),any()) } answers {
            println("onFilterByPeriod called with ${args[0]} and ${args[1]}")
        }
    }

    private fun selectDateInPicker(dateFormatted: String) {
        composeTestRule.apply {
            onNodeWithText(
                text = dateFormatted,
                substring = true,
                ignoreCase = true,
                useUnmergedTree = true
            ).performClick()
            onNodeWithTag(confirmTextButton).performClick()
            waitForIdle()
        }
    }

    @Test
    fun shouldDisplayAPartirDeAndAteFieldsAndFilterButton_whenPeriodFilterIsCalled() {
        composeTestRule.onNodeWithTag(periodFilterComposable).assertExists()
        composeTestRule.onNodeWithTag(periodFilterStartDateField).assertExists()
        composeTestRule.onNodeWithTag(periodFilterEndDateField).assertExists()
    }

    @Test
    fun shouldDisplayYesterdayDate_whenYesterdayIsSelected_inStartDateField() {
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(yesterdayFormatted)
            onNodeWithText(yesterdayDate).assert(hasText(yesterdayDate))
        }
    }

    @Test
    fun shouldDisplayTodayDate_whenTodayIsSelected_inEndDateField() {
        composeTestRule.apply {
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(todayFormatted)
            onNodeWithText(currentDate).assert(hasText(currentDate))
        }
    }

    @Test
    fun shouldUnableButton_whenNoDatesAreSelected() {
        composeTestRule.apply {
            onNodeWithText(initialDateLabel)
                .assertExists()
                .assert(hasText(""))
            onNodeWithText(finalDateLabel)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldUnableButton_whenOnlyInitialDateIsSelected() {
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(yesterdayFormatted)
            onNodeWithText(finalDateLabel)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldUnableButton_whenOnlyEndDateIsSelected() {
        composeTestRule.apply {
            onNodeWithText(initialDateLabel)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(todayFormatted)
            onNodeWithTag(periodFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldActiveButton_whenBothDatesAreSelected() {
        composeTestRule.apply {
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(yesterdayFormatted)
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(todayFormatted)
            onNodeWithTag(periodFilterButton).assertIsEnabled()
        }
    }

    @Test
    fun shouldFilter_whenFilterButtonIsActiveAndClicked() {
        composeTestRule.apply{
            onNodeWithTag(periodFilterStartDateField).performClick()
            selectDateInPicker(yesterdayFormatted)
            onNodeWithTag(periodFilterEndDateField).performClick()
            selectDateInPicker(todayFormatted)
            onNodeWithTag(periodFilterButton).performClick()
            verify { mockOnFilterByPeriod(any(),any()) }
        }
    }
}