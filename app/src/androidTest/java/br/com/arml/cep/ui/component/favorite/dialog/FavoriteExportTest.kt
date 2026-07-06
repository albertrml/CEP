package br.com.arml.cep.ui.component.favorite.dialog

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.favorite.dialog.FavoriteExport
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteExportTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val favoriteExportTag = ctx.getString(R.string.favoriteExport_component_testTag)
    private val dialogTitleText = ctx.getString(R.string.favoriteExport_title_text)
    private val dialogContentText = ctx.getString(R.string.favoriteExport_message_text)
    private val confirmButtonText = ctx.getString(R.string.cepAlertdialog_confirmButton_text)
    private val dismissButtonText = ctx.getString(R.string.cepAlertdialog_dismissButton_text)

    @Test
    fun favoriteExportTest_shouldDisplaysTitleContentConfirmAndCancelButton(){
        composeTestRule.apply{
            setContent {
                FavoriteExport(
                    isVisibility = true,
                    onDismissRequest = {},
                    onConfirmationRequest = {}
                )
            }
            onNodeWithTag(favoriteExportTag)
                .assertExists()
            onNodeWithText(dialogTitleText, useUnmergedTree = true)
                .assertExists()
            onNodeWithText(dialogContentText, useUnmergedTree = true)
                .assertExists()
        }
    }

    @Test
    fun favoriteExportTest_shouldExists_whenIsVisibilityIsTrue(){
        composeTestRule.apply {
            setContent {
                FavoriteExport(
                    isVisibility = true,
                    onDismissRequest = {},
                    onConfirmationRequest = {}
                )
            }
            onNodeWithTag(favoriteExportTag).assertExists()
        }
    }

    @Test
    fun favoriteExportTest_shouldNotExists_whenIsVisibilityIsFalse(){
        composeTestRule.apply {
            setContent {
                FavoriteExport(
                    isVisibility = false,
                    onDismissRequest = {},
                    onConfirmationRequest = {}
                )
            }
            onNodeWithTag(favoriteExportTag).assertDoesNotExist()
        }
    }

    @Test
    fun favoriteExportTest_shouldInvokesOnDismissRequestCallback_whenConfirmButtonIsClicked(){
        val onDismissRequest: () -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                FavoriteExport(
                    isVisibility = true,
                    onDismissRequest = onDismissRequest,
                    onConfirmationRequest = {}
                )
            }
            onNodeWithText(dismissButtonText)
                .assertExists()
                .performClick()
            verify(exactly = 1){ onDismissRequest() }
        }
    }

    @Test
    fun favoriteExportTest_shouldInvokesOnConfirmationRequestCallback_whenConfirmButtonIsClicked(){
        val onConfirmationRequest: () -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                FavoriteExport(
                    isVisibility = true,
                    onDismissRequest = {},
                    onConfirmationRequest = onConfirmationRequest
                )
            }
            onNodeWithText(confirmButtonText)
                .assertExists()
                .performClick()
            verify(exactly = 1){ onConfirmationRequest() }
        }
    }
}