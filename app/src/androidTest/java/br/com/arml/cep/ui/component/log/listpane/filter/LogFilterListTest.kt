package br.com.arml.cep.ui.component.log.listpane.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.log.listpane.filter.LogFilterList
import br.com.arml.cep.ui.utils.LogFilterOption
import br.com.arml.cep.ui.utils.LogFilterOption.ByCep
import br.com.arml.cep.ui.utils.LogFilterOption.ByFinalDate
import br.com.arml.cep.ui.utils.LogFilterOption.ByInitialDate
import br.com.arml.cep.ui.utils.LogFilterOption.ByRangeDate
import br.com.arml.cep.ui.utils.LogFilterOption.None
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogFilterListTest {
    @get:Rule
    val componentTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    fun getMarkFilterSelectedDescription(filter: LogFilterOption) =
        ctx.getString(
            R.string.logFilterChip_filterSelected_description,
            filter.name
        )

    @Test
    fun logFilterList_shouldBeAbleSelectedJustOneFilterPerTurn_whenFilterChipIsClicked(){
        val filters = listOf(
            None,
            ByCep,
            ByInitialDate,
            ByFinalDate,
            ByRangeDate
        )

        val unmarkedFilters = filters.toMutableList()

        var selectedFilter by mutableStateOf(filters.first())

        componentTestRule.apply {
            setContent {
                LogFilterList(
                    filters = filters,
                    selectedFilter = selectedFilter,
                    onSelectedFilter = { selectedFilter = it }
                )
            }
            filters.reversed().forEach { filter ->
                val contentDescription = getMarkFilterSelectedDescription(filter)
                onNode(hasContentDescription(contentDescription)).assertIsNotDisplayed()
                onNodeWithText(filter.name).performClick()
                onNode(hasContentDescription(contentDescription)).assertIsDisplayed()

                unmarkedFilters.remove(filter)

                unmarkedFilters.forEach { unmarkedFilter ->
                    val contentDescription = getMarkFilterSelectedDescription(unmarkedFilter)
                    onNode(hasContentDescription(contentDescription)).assertIsNotDisplayed()
                }
                assertThat(filter).isEqualTo(selectedFilter)
            }
        }
    }
}