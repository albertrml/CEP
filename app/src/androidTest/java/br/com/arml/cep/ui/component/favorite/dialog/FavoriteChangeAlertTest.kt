package br.com.arml.cep.ui.component.favorite.dialog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.dialog.FavoriteChangeAlert
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteChangeAlertTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val favoriteChangeAlert = ctx.getString(R.string.favoriteChangeAlert_component_testTag)
    private val confirmButtonAlertText = ctx.getString(R.string.cepAlertdialog_confirmButton_text)
    private val dismissButtonAlertText = ctx.getString(R.string.cepAlertdialog_dismissButton_text)

    @Test
    fun favoriteChangeAlert_shouldDisplaysTitleContentConfirmAndCancelButton(){
        val place = mockFavoritePlaces.first()
        val expectedTitle = place.cep.text
        val expectedText = ctx.getString(
            R.string.favoriteChangeAlert_message_text,
            expectedTitle
        )

        composeTestRule.apply{
            setContent {
                FavoriteChangeAlert(
                    place = place,
                    onConfirmationRequest = {},
                    onDismissRequest = {}
                )
            }

            onNodeWithText(expectedTitle, useUnmergedTree = true)
                .assertExists()
            onNodeWithText(expectedText, useUnmergedTree = true)
                .assertExists()
            onNodeWithText(dismissButtonAlertText, useUnmergedTree = true)
                .assertExists()
                .assertIsEnabled()
            onNodeWithText(confirmButtonAlertText, useUnmergedTree = true)
                .assertExists()
                .assertIsEnabled()
        }
    }

    @Test
    fun favoriteChangeAlert_shouldBeVisible_whenPlaceIsNotNull(){
        val place = mockFavoritePlaces.first()
        composeTestRule.apply{
            setContent {
                FavoriteChangeAlert(
                    place = place,
                    onConfirmationRequest = {},
                    onDismissRequest = {}
                )
            }
            onNodeWithTag(favoriteChangeAlert)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun favoriteChangeAlert_shouldBeVisible_whenIsVisibilityIsFalse(){
        composeTestRule.apply{
            setContent {
                FavoriteChangeAlert(
                    place = null,
                    onConfirmationRequest = {},
                    onDismissRequest = {}
                )
            }
            onNodeWithTag(favoriteChangeAlert)
                .assertDoesNotExist()
        }
    }

    @Test
    fun favoriteChangeAlert_shouldInvokesOnDismissRequestCallback_whenConfirmButtonIsClicked(){
        val place = mockFavoritePlaces.first()
        val mockOnDismissRequest: () -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                FavoriteChangeAlert(
                    place = place,
                    onConfirmationRequest = {},
                    onDismissRequest = mockOnDismissRequest
                )
            }

            onNodeWithText(dismissButtonAlertText)
                .assertExists()
                .assertIsEnabled()
                .performClick()
            verify(exactly = 1) { mockOnDismissRequest() }
        }
    }

    @Test
    fun favoriteChangeAlert_shouldInvokesOnConfirmationRequestCallback_whenConfirmButtonIsClicked(){
        val place = mockFavoritePlaces.first()
        val mockOnConfirmationRequest: () -> Unit = mockk(relaxed = true)
        composeTestRule.apply{
            setContent {
                FavoriteChangeAlert(
                    place = place,
                    onConfirmationRequest = mockOnConfirmationRequest,
                    onDismissRequest = {}
                )
            }

            onNodeWithText(confirmButtonAlertText)
                .assertExists()
                .assertIsEnabled()
                .performClick()
            verify(exactly = 1) { mockOnConfirmationRequest() }
        }
    }
}