package br.com.arml.cep.ui.component.common

import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.dialog.DeleteAllComponent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteAllComponentTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var deleteAllComponent: String
    private lateinit var deleteAllComponentDivider: String
    private lateinit var deleteAllComponentButton: String
    private lateinit var logScreenDeleteAllLogAlert: String
    private lateinit var logScreenDeleteAllLogAlertTitle: String
    private lateinit var logScreenDeleteAllLogAlertMessage: String
    private lateinit var logScreenDeleteAllLogAlertConfirmButton: String
    private lateinit var logScreenDeleteAllLogAlertDismissButton: String

    private val onConfirmDeleteAllEntries: () -> Unit = mockk(relaxed = true        )

    @Before
    fun steUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            deleteAllComponent = getString(R.string.deleteAllComponent_component_testTag)
            deleteAllComponentDivider = getString(R.string.deleteAllComponent_horizontalDivider_testTag)
            deleteAllComponentButton = getString(R.string.deleteAllComponent_button_testTag)
            logScreenDeleteAllLogAlert = getString(R.string.cepAlertdialog_component_testTag)
            logScreenDeleteAllLogAlertTitle = getString(R.string.cepAlertDialog_mockTitle_text)
            logScreenDeleteAllLogAlertMessage = getString(R.string.cepAlertDialog_mockContent_text)
            logScreenDeleteAllLogAlertConfirmButton = getString(R.string.cepAlertdialog_confirmButton_text)
            logScreenDeleteAllLogAlertDismissButton = getString(R.string.cepAlertdialog_dismissButton_text)
        }

        every { onConfirmDeleteAllEntries() } answers { println("onConfirmDeleteAllEntries") }

        showDeleteAllComponent()
    }

    fun showDeleteAllComponent(){
        composeTestRule.setContent {
            DeleteAllComponent(
                deleteLogAlertTitleId = R.string.cepAlertDialog_mockTitle_text,
                deleteLogAlertTextId = R.string.cepAlertDialog_mockContent_text,
                onConfirmDeleteAllEntries = onConfirmDeleteAllEntries,
            )
        }
    }

    @Test
    fun shouldDeleteAllEntries_whenDeleteAllLogAlertConfirmButtonIsClicked(){
        composeTestRule.apply {
            onNodeWithTag(deleteAllComponentButton).performClick()
            waitForIdle()
            onNodeWithText(logScreenDeleteAllLogAlertConfirmButton).performClick()
            verify { onConfirmDeleteAllEntries() }
        }
    }

    @Test
    fun shouldDisplayDividerAndButton_whenDeleteAllComponentIsCalled(){
        composeTestRule.apply {
            onNodeWithTag(deleteAllComponent).assertExists()
            onNodeWithTag(deleteAllComponentDivider).assertExists()
            onNodeWithTag(deleteAllComponentButton).assertExists()
            onNodeWithTag(logScreenDeleteAllLogAlert).assertIsNotDisplayed()
        }
    }

    @Test
    fun shouldDisplayDeleteAllLogAlert_whenDeleteAllComponentButtonIsClicked(){
        composeTestRule.apply {
            onNodeWithTag(deleteAllComponentButton).performClick()
            onNodeWithTag(logScreenDeleteAllLogAlert).assertExists()
            onNodeWithText(logScreenDeleteAllLogAlertTitle).assertExists()
            onNodeWithText(logScreenDeleteAllLogAlertMessage).assertExists()
            onNodeWithText(logScreenDeleteAllLogAlertConfirmButton).assertExists()
            onNodeWithText(logScreenDeleteAllLogAlertDismissButton).assertExists()
        }
    }

    @Test
    fun shouldNotDisplayDeleteAllLogAlert_whenDeleteAllLogAlertDismissButtonIsClicked(){
        composeTestRule.apply {
            onNodeWithTag(deleteAllComponentButton).performClick()
            waitForIdle()
            onNodeWithText(logScreenDeleteAllLogAlertDismissButton).performClick()
            onNodeWithTag(logScreenDeleteAllLogAlert).assertIsNotDisplayed()
        }
    }
}