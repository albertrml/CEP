package br.com.arml.cep.ui.component

import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.utils.toFormattedBR
import br.com.arml.cep.ui.screen.component.common.DatePickerFieldToModal
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DatePickerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var datePickerModal: String
    private lateinit var cancelTextButton: String
    private lateinit var confirmTextButton: String
    private lateinit var datePickerField: String

    private val mockOnSelectDate: (Long) -> Unit = mockk(relaxed = true)
    private val currentTimeMillis = System.currentTimeMillis()
    private val currentDate = currentTimeMillis.toFormattedBR()
    private val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())

    private val dateFormatted = Instant.ofEpochMilli(currentTimeMillis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    @Before
    fun setUp(){
        datePickerModal = ctx.getString(R.string.testTag_datePickerModal)
        datePickerField = ctx.getString(R.string.testTag_datePickerField)
        cancelTextButton = ctx.getString(R.string.testTag_datePicker_cancelTextButton)
        confirmTextButton = ctx.getString(R.string.testTag_datePicker_confirmTextButton)
        every { mockOnSelectDate(any()) } answers { println("onSelectDate called with ${args[0]}") }
        composeTestRule.setContent {
            DatePickerFieldToModal(
                label = datePickerField,
                onSelectDate = mockOnSelectDate
            )
        }
    }

    @Test
    fun shouldOpenDatePickerModal_whenDatePickerFieldIsClicked() {
        composeTestRule.apply {
            onNodeWithTag(datePickerField)
                .assertExists()
                .performClick()
            waitForIdle()
            onNodeWithTag(datePickerModal).assertExists()
            onNodeWithTag(cancelTextButton).assertExists()
            onNodeWithTag(confirmTextButton).assertExists()
        }
    }

    @Test
    fun shouldCloseModal_whenCancelIsClicked(){
        composeTestRule.apply {
            onNodeWithTag(datePickerField).performClick()
            waitForIdle()
            onNodeWithTag(cancelTextButton).performClick()
            onNodeWithTag(datePickerModal).assertDoesNotExist()
        }
    }

    @Test
    fun shouldDisplayDate_whenPerformClickOnDatePickerFieldAndConfirmIsClicked(){
        composeTestRule.apply {
            onNodeWithTag(datePickerField).performClick()
            waitForIdle()
            onNodeWithText(
                text = dateFormatted,
                substring = true,
                ignoreCase = true,
                useUnmergedTree = true
            ).performClick()
            onNodeWithTag(confirmTextButton).performClick()
            waitForIdle()
            //onAllNodes(isRoot())[0].printToLog("DialogRootDatePickerTest $currentDate")
            onNodeWithTag(datePickerField).assert(hasText(currentDate))
        }
    }
}