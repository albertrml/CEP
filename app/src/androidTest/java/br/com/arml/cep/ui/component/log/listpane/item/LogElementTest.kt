package br.com.arml.cep.ui.component.log.listpane.item

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.ui.screen.component.log.listpane.item.LogElement
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogElementTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    private val log = mockLogEntries.first()

    private val contentTag = ctx.getString(
        R.string.logElementContent_component_testTag,
        log.toString()
    )
    private val deleteButtonTag = ctx.getString(
        R.string.logElement_deleteButton_testTag
    )

    @Test
    fun logElement_shouldDisplayContentAndButton(){
        composeTestRule.apply {
            setContent {
                LogElement(
                    log = log,
                    onClickToDelete = {},
                    onClickToDetail = {}
                )
            }
            onNodeWithTag(contentTag, useUnmergedTree = true).assertIsDisplayed()
            onNodeWithTag(deleteButtonTag, useUnmergedTree = true).assertIsDisplayed()
        }
    }

    @Test
    fun logElement_shouldInvokesOnClickToDetailCallback_whenLogElementIsClicked(){
        val mockOnClickToDetail: (Log) -> Unit = mockk(relaxed = true)
        val logElementTag = ctx.getString(
            R.string.logElement_component_testTag,
            log.toString()
        )
        composeTestRule.apply {
            setContent {
                LogElement(
                    modifier = Modifier.testTag(logElementTag),
                    log = log,
                    onClickToDelete = {},
                    onClickToDetail = mockOnClickToDetail
                )
            }
            onNodeWithTag(logElementTag).performClick()
            verify(exactly = 1) { mockOnClickToDetail(log) }
        }
    }

    @Test
    fun logElement_shouldInvokesOnClickToDeleteCallback_whenDeleteButtonIsClicked(){
        val mockOnClickToDelete: (Log) -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                LogElement(
                    log = log,
                    onClickToDelete = mockOnClickToDelete,
                    onClickToDetail = {}
                )
            }
            onNodeWithTag(deleteButtonTag).performClick()
            verify(exactly = 1) { mockOnClickToDelete(log) }
        }
    }
}