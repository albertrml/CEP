package br.com.arml.cep.ui.component.common.filter

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.adjustDay
import br.com.arml.cep.model.utils.toFormattedBR
import br.com.arml.cep.ui.screen.component.common.filter.DateFilter
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

class DateFilterTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    /*** DatePicker ***/
    private val confirmTextButton = ctx.getString(R.string.datePickerModal_confirmButton_testTag)

    /*** Filter Composable ***/
    private val singleDateFilterComposable = ctx
        .getString(R.string.dateFilter_component_testTag)
    private val formatter = DateTimeFormatter
        .ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())

    /*** Date Field ***/
    private val singleDateFilterDateField = ctx
        .getString(R.string.dateFilter_startDateField_testTag)
    private val todayInMillis = System.currentTimeMillis().adjustDay()
    private val today = todayInMillis.toFormattedBR()
    private val formattedToday = Instant.ofEpochMilli(todayInMillis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    /*** Filter Button ***/
    private val singleDateFilterButton = ctx
        .getString(R.string.dateFilter_filterButton_testTag)
    private val mockOnFilterByDate: (Long) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp(){
        composeTestRule.setContent {
            DateFilter(
                labelId = R.string.logFilterComponent_initialDate_label,
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
            verify { mockOnFilterByDate(todayInMillis) }
        }
    }
}