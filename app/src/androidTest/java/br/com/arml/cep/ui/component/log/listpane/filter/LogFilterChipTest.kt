package br.com.arml.cep.ui.component.log.listpane.filter

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.log.listpane.filter.LogFilterChip
import br.com.arml.cep.ui.utils.LogFilterOption
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogFilterChipTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val expectedLabel = LogFilterOption.ByRangeDate
    private val logFilterChipTag = ctx.getString(R.string.logFilterChip_component_testTag)
    private val logFilterSelectedDescription = ctx.getString(
        R.string.logFilterChip_filterSelected_description,
        expectedLabel.name
    )

    @Test
    fun logFilterChip_shouldDisplayOnlyText_whenFilterIsNotSelected() {
        composeTestRule.apply {
            setContent {
                LogFilterChip(
                    modifier = Modifier.testTag(logFilterChipTag),
                    labelFilter = expectedLabel,
                    isSelected = false,
                    onSelected = {}
                )
            }
            onNodeWithTag(logFilterChipTag).assertIsDisplayed()
            onNodeWithText(expectedLabel.name).assertIsDisplayed()
            onNode(hasContentDescription(logFilterSelectedDescription))
                .assertIsNotDisplayed()
        }
    }

    @Test
    fun logFilterChip_shouldDisplayTextAndCheckMark_whenFilterIsSelected() {
        composeTestRule.apply {
            setContent {
                LogFilterChip(
                    modifier = Modifier.testTag(logFilterChipTag),
                    labelFilter = expectedLabel,
                    isSelected = true,
                    onSelected = {}
                )
            }
            onNodeWithTag(logFilterChipTag).assertIsDisplayed()
            onNodeWithText(expectedLabel.name).assertIsDisplayed()
            onNode(hasContentDescription(logFilterSelectedDescription))
                .assertIsDisplayed()
        }
    }

    @Test
    fun logFilterChip_shouldInvokesOnSelectedCallback_whenFilterIsSelected() {
        val mockOnSelected: (LogFilterOption) -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                LogFilterChip(
                    modifier = Modifier.testTag(logFilterChipTag),
                    labelFilter = expectedLabel,
                    isSelected = true,
                    onSelected = mockOnSelected
                )
            }
            onNodeWithTag(logFilterChipTag).performClick()
            verify(exactly = 1) { mockOnSelected(expectedLabel) }
        }
    }
}