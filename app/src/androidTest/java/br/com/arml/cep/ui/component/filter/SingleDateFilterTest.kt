package br.com.arml.cep.ui.component.filter

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.toFormattedBR
import br.com.arml.cep.ui.screen.component.common.SingleDateFilter
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

class SingleDateFilterTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /*** DatePicker ***/
    private lateinit var datePickerModal: String
    private lateinit var confirmTextButton: String
    private lateinit var singleDateLabel: String

    /*** Filter Composable ***/
    private lateinit var singleDateFilterComposable: String
    private val formatter = DateTimeFormatter
        .ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())

    /*** Date Field ***/
    private lateinit var singleDateFilterDateField: String
    private val todayInMillis = System.currentTimeMillis()
    private val today = todayInMillis.toFormattedBR()
    private val formattedToday = Instant.ofEpochMilli(todayInMillis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    /*** Filter Button ***/
    private lateinit var singleDateFilterButton: String
    private val mockOnFilterByDate: (Long) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            /*** Filter Composable ***/
            singleDateFilterComposable = getString(R.string.testTag_singleDateFilter_composable)
            singleDateFilterDateField = getString(R.string.testTag_singleDateFilter_field)
            singleDateFilterButton = getString(R.string.testTag_singleDateFilter_button)

            /*** DatePicker ***/
            datePickerModal = getString(R.string.testTag_datePickerModal)
            singleDateLabel = getString(R.string.log_filter_initial_date_label)
            confirmTextButton = getString(R.string.testTag_datePicker_confirmTextButton)
        }

        composeTestRule.setContent {
            SingleDateFilter(
                labelId = R.string.log_filter_initial_date_label,
                onFilterByDate = mockOnFilterByDate
            )
        }

        every { mockOnFilterByDate(any()) } answers {
            println("onFilterByDate called with ${args[0]}")
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
    fun shouldDisplayDateFieldAndFilterButton_whenSingleDateFilterIsCalled() {
        composeTestRule.apply{
            onNodeWithTag(singleDateFilterComposable).assertExists()
            onNodeWithTag(singleDateFilterDateField).assertExists()
            onNodeWithTag(singleDateFilterButton).assertExists()
        }
    }

    @Test
    fun shouldDisplayTodayDate_whenTodayIsSelected_inDateField() {
        composeTestRule.apply {
            onNodeWithTag(singleDateFilterDateField).performClick()
            selectDateInPicker(formattedToday)
            onNodeWithText(today).assertExists()
        }
    }

    @Test
    fun shouldUnableButton_whenDateIsNotSelected() {
        composeTestRule.apply {
            onNodeWithTag(singleDateFilterDateField)
                .assertExists()
                .assert(hasText(""))
            onNodeWithTag(singleDateFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldActiveButton_whenDateIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(singleDateFilterDateField).performClick()
            selectDateInPicker(formattedToday)
            onNodeWithTag(singleDateFilterButton).assertExists()
        }
    }

    @Test
    fun shouldFilter_whenDateIsSelectedAndFilterButtonIsClicked() {
        composeTestRule.apply {
            onNodeWithTag(singleDateFilterDateField).performClick()
            selectDateInPicker(formattedToday)
            onNodeWithTag(singleDateFilterButton).performClick()
            verify { mockOnFilterByDate(any()) }
        }
    }
}