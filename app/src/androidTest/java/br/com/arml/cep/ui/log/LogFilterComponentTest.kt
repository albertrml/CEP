package br.com.arml.cep.ui.log

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.log.LogFilterComponent
import br.com.arml.cep.ui.utils.LogFilterOption
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LogFilterComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var logFilterComponent: String
    private val logFilterNoneChip: String = LogFilterOption.None.name
    private val logFilterByCepChip: String = LogFilterOption.ByCep.name
    private val logFilterByInitialDateChip: String = LogFilterOption.ByInitialDate.name
    private val logFilterByFinalDateChip: String = LogFilterOption.ByFinalDate.name
    private val logFilterByRangeDateChip: String = LogFilterOption.ByRangeDate.name

    /*** Cep Filter Components ***/
    private lateinit var cepFilterTextField: String
    private lateinit var cepFilterSearchButton: String

    /*** Period Filter Components ***/
    private lateinit var periodFilterStartDateField: String
    private lateinit var periodFilterEndDateField: String
    private lateinit var periodFilterButton: String

    /*** Single Date Filter Components ***/
    private lateinit var singleDateFilterField: String
    private lateinit var singleDateFilterButton: String

    /*** Title Filter Components ***/
    private lateinit var titleFilterField: String
    private lateinit var titleFilterButton: String

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.resources.apply {
            logFilterComponent = getString(R.string.testTag_logFilter_composable)

            cepFilterTextField = getString(R.string.testTag_cepFilter_searchField)
            cepFilterSearchButton = getString(R.string.testTag_cepFilter_searchButton)

            periodFilterStartDateField = getString(R.string.testTag_periodFilter_startDateField)
            periodFilterEndDateField = getString(R.string.testTag_periodFilter_endDateField)
            periodFilterButton = getString(R.string.testTag_periodFilter_button)

            singleDateFilterField = getString(R.string.testTag_singleDateFilter_field)
            singleDateFilterButton = getString(R.string.testTag_singleDateFilter_button)

            titleFilterField = getString(R.string.testTag_titleFilter_field)
            titleFilterButton = getString(R.string.testTag_titleFilter_button)
        }

        displayLogFilterComponent()
    }

    fun displayLogFilterComponent(){
        composeTestRule.setContent {
            LogFilterComponent(
                onFilterByCep = {},
                onFilterByInitialDate = {},
                onFilterByFinalDate = {},
                onFilterByRangeDate = { _, _ -> },
                onNoneFilter = {}
            )
        }
    }

    @Test
    fun shouldDisplayAllFilterOptions_whenComponentIsDisplayedAndScrolled(){
        composeTestRule.apply{
            onRoot().printToLog("LogFilterComponentTest")
            onNodeWithTag(logFilterComponent)
                .assertExists()
                .performScrollToNode(matcher = hasText(logFilterByCepChip)).assertExists()
                .performScrollToNode(matcher = hasText(logFilterByInitialDateChip)).assertExists()
                .performScrollToNode(matcher = hasText(logFilterByFinalDateChip)).assertExists()
                .performScrollToNode(matcher = hasText(logFilterByRangeDateChip)).assertExists()
                .performScrollToNode(matcher = hasText(logFilterNoneChip)).assertExists()
        }
    }

    @Test
    fun shouldDisplayCepFilterComponents_whenCepFilterOptionIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(logFilterComponent)
                .performScrollToNode(matcher = hasText(logFilterByCepChip))
            onNodeWithText(logFilterByCepChip).performClick()
            waitForIdle()
            onNodeWithTag(cepFilterTextField).assertExists()
            onNodeWithTag(cepFilterSearchButton).assertExists()
        }
    }

    @Test
    fun shouldDisplaySingleDateFilterComponents_whenInitialDateFilterIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(logFilterComponent)
                .performScrollToNode(matcher = hasText(logFilterByInitialDateChip))
            onNodeWithText(logFilterByInitialDateChip).performClick()
            waitForIdle()
            onNodeWithTag(singleDateFilterField).assertExists()
            onNodeWithTag(singleDateFilterButton).assertExists()
        }
    }

    @Test
    fun shouldDisplaySingleDateFilterComponents_whenFinalDateFilterIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(logFilterComponent)
                .performScrollToNode(matcher = hasText(logFilterByFinalDateChip))
            onNodeWithText(logFilterByFinalDateChip).performClick()
            waitForIdle()
            onNodeWithTag(singleDateFilterField).assertExists()
            onNodeWithTag(singleDateFilterButton).assertExists()
        }
    }

    @Test
    fun shouldDisplayPeriodFilterComponents_whenRangeDateFilterIsSelected(){
        composeTestRule.apply {
            onNodeWithTag(logFilterComponent)
                .performScrollToNode(matcher = hasText(logFilterByRangeDateChip))
            onNodeWithText(logFilterByRangeDateChip).performClick()
            waitForIdle()
            onNodeWithTag(periodFilterStartDateField).assertExists()
            onNodeWithTag(periodFilterEndDateField).assertExists()
            onNodeWithTag(periodFilterButton).assertExists()
        }
    }
}