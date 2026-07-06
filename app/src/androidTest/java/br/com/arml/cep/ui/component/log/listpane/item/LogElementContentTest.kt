package br.com.arml.cep.ui.component.log.listpane.item

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.model.utils.toFormattedUTC
import br.com.arml.cep.ui.screen.component.log.listpane.item.LogElementContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogElementContentTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun logElementContentTest_shouldDisplayIconAndText(){
        val log = mockLogEntries.first()
        val expectedText = "CEP: ${log.cep.text}"
        val expectedDate = log.timestamp.toFormattedUTC()
        val cepDescription = ctx.getString(
            R.string.logElementContent_cepField_description,
            log.cep.text
        )
        val dateDescription = ctx.getString(
            R.string.logElementContent_timestampField_description,
            log.timestamp.toFormattedUTC()
        )

        composeTestRule.apply {
            setContent {
                LogElementContent(
                    log = mockLogEntries.first()
                )
            }
            onNodeWithText(expectedText).assertIsDisplayed()
            onNodeWithText(expectedDate).assertIsDisplayed()
            onNode(hasContentDescription(cepDescription)).assertIsDisplayed()
            onNode(hasContentDescription(dateDescription)).assertIsDisplayed()
        }
    }
}