package br.com.arml.cep.ui.component.log.listpane.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.log.listpane.filter.LogFilterComponent
import br.com.arml.cep.ui.utils.LogFilterOption
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogFilterComponentTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val cepFilterTag = ctx.getString(R.string.cepFilter_component_testTag)
    private val initialFilterTag = ctx.getString(R.string.logFilterComponent_initialDate_testTag)
    private val finalFilterTag = ctx.getString(R.string.logFilterComponent_finalDate_testTag)
    private val periodFilterTag = ctx.getString(R.string.periodFilter_composable_testTag)

    @Test
    fun logFilterComponent_shouldDisplayCepFilter_whenCepChipIsClicked(){
        val cepFilterText = LogFilterOption.ByCep.name
        var selectedFilter by mutableStateOf<LogFilterOption>(LogFilterOption.None)
        composeTestRule.apply{
            setContent {
                LogFilterComponent(
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    onFilterByCep = {},
                    onFilterByInitialDate = {},
                    onFilterByFinalDate = {},
                    onFilterByRangeDate = { _, _ -> },
                    onNoneFilter = {}
                )
            }
            onRoot().printToLog("logFilterComponent")
            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()

            onNodeWithText(cepFilterText).performClick()

            onNodeWithTag(cepFilterTag).assertIsDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()
        }
    }

    @Test
    fun logFilterComponent_shouldDisplayDateFilter_whenAPartirDeChipIsClicked(){
        val filterText = LogFilterOption.ByInitialDate.name
        var selectedFilter by mutableStateOf<LogFilterOption>(LogFilterOption.None)
        composeTestRule.apply{
            setContent {
                LogFilterComponent(
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    onFilterByCep = {},
                    onFilterByInitialDate = {},
                    onFilterByFinalDate = {},
                    onFilterByRangeDate = { _, _ -> },
                    onNoneFilter = {}
                )
            }
            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()

            onNodeWithText(filterText).performClick()

            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()
        }
    }

    @Test
    fun logFilterComponent_shouldDisplayDateFilter_whenAteChipIsClicked(){
        val filterText = LogFilterOption.ByFinalDate.name
        var selectedFilter by mutableStateOf<LogFilterOption>(LogFilterOption.None)
        composeTestRule.apply{
            setContent {
                LogFilterComponent(
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    onFilterByCep = {},
                    onFilterByInitialDate = {},
                    onFilterByFinalDate = {},
                    onFilterByRangeDate = { _, _ -> },
                    onNoneFilter = {}
                )
            }
            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()

            onNodeWithText(filterText).performClick()

            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()
        }
    }

    @Test
    fun logFilterComponent_shouldDisplayRangeFilter_whenAteChipIsClicked(){
        val filterText = LogFilterOption.ByRangeDate.name
        var selectedFilter by mutableStateOf<LogFilterOption>(LogFilterOption.None)
        composeTestRule.apply{
            setContent {
                LogFilterComponent(
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    onFilterByCep = {},
                    onFilterByInitialDate = {},
                    onFilterByFinalDate = {},
                    onFilterByRangeDate = { _, _ -> },
                    onNoneFilter = {}
                )
            }
            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsNotDisplayed()

            onNodeWithText(filterText).performClick()

            onNodeWithTag(cepFilterTag).assertIsNotDisplayed()
            onNodeWithTag(initialFilterTag).assertIsNotDisplayed()
            onNodeWithTag(finalFilterTag).assertIsNotDisplayed()
            onNodeWithTag(periodFilterTag).assertIsDisplayed()
        }
    }
}