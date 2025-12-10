package br.com.arml.cep.ui.component.common.datepicker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.toFormattedBR
import br.com.arml.cep.ui.screen.component.common.datepicker.DatePickerField
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.System.currentTimeMillis
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DatePickerFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val datePickerField = ctx.getString(R.string.datePickerField_component_testTag)
    private val datePickerModal = ctx.getString(R.string.datePickerModal_component_testTag)
    private val confirmButtonDatePickerModalTag = ctx
        .getString(R.string.datePickerModal_confirmButton_testTag)
    private val dismissButtonDatePickerModalTag = ctx
        .getString(R.string.datePickerModal_dismissButton_testTag)

    private val currentDate = currentTimeMillis().toFormattedBR()
    private val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())

    private val dateFormatted = Instant.ofEpochMilli(currentTimeMillis())
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    private val mockOnSelectDate: (Long) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp(){
        every { mockOnSelectDate(any()) } answers { println("onSelectDate called with ${args[0]}") }
    }

    @Test
    fun datePickerField_shouldOpenDatePickerModal_whenDatePickerFieldIsClicked() {
        var date by mutableStateOf<Long?>(null)
        val onDateChange = { newDate: Long? -> date = newDate }
        composeTestRule.apply {
            setContent {
                DatePickerField(
                    modifier = Modifier.testTag(datePickerField),
                    label = "Date",
                    date = date,
                    onSelectDate = onDateChange
                )
            }

            onNodeWithTag(datePickerField)
                .assertExists()
                .performClick()
            waitForIdle()
            onNodeWithTag(datePickerModal).assertExists()
            onNodeWithTag(dismissButtonDatePickerModalTag).assertExists()
            onNodeWithTag(confirmButtonDatePickerModalTag).assertExists()
        }
    }

    @Test
    fun datePickerField_shouldCloseModal_whenCancelIsClicked(){
        var date by mutableStateOf<Long?>(null)
        val onDateChange = { newDate: Long? -> date = newDate }
        composeTestRule.apply {
            setContent {
                DatePickerField(
                    modifier = Modifier.testTag(datePickerField),
                    label = "Date",
                    date = date,
                    onSelectDate = onDateChange
                )
            }
            onNodeWithTag(datePickerField).performClick()
            waitForIdle()
            onNodeWithTag(dismissButtonDatePickerModalTag).performClick()
            onNodeWithTag(datePickerModal).assertDoesNotExist()
        }
    }

    @Test
    fun datePickerField_shouldDisplayDate_whenPerformClickOnDatePickerFieldAndConfirmIsClicked(){
        var date by mutableStateOf<Long?>(null)
        val onDateChange = { newDate: Long? -> date = newDate }
        composeTestRule.apply {
            setContent {
                DatePickerField(
                    modifier = Modifier.testTag(datePickerField),
                    label = "Date",
                    date = date,
                    onSelectDate = onDateChange
                )
            }
            onNodeWithTag(datePickerField).performClick()
            waitForIdle()
            onNodeWithText(
                text = dateFormatted,
                substring = true,
                ignoreCase = true,
                useUnmergedTree = true
            ).performClick()
            onNodeWithTag(confirmButtonDatePickerModalTag).performClick()
            waitForIdle()
            onNodeWithTag(datePickerField).assert(hasText(currentDate))
        }
    }
}