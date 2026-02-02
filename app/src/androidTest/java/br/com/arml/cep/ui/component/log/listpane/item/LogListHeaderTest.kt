package br.com.arml.cep.ui.component.log.listpane.item

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.arml.cep.ui.screen.component.log.listpane.item.LogListHeader
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogListHeaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun logListHeader_shouldDisplayDateCorrectly() {
        val testDate = "17 de Dezembro de 2025"

        composeTestRule.setContent {
            LogListHeader(date = testDate)
        }

        // Verify that the text with the date is displayed
        composeTestRule.onNodeWithText(testDate).assertIsDisplayed()
    }
}