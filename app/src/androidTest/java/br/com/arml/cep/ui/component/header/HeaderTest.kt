package br.com.arml.cep.ui.component.header

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.theme.dimens
import com.google.common.truth.Truth
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class HeaderTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()
    lateinit var titleHeaderTag: String
    lateinit var iconHeaderTag: String

    val mockOnClickLogo: () -> Unit = mockk(relaxed = true)
    val mockMenu: @Composable () -> Unit = mockk(relaxed = true)

    @Before
    fun setup(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            titleHeaderTag = getString(R.string.header_title_testTag)
            iconHeaderTag = getString(R.string.header_logo_description)
        }
    }

    @Test
    fun header_shouldShowTitleAndIcon_whenItIsRendered() = runTest {
        composeTestRule.apply {
            setContent {
                Header(
                    modifier = Modifier.padding(MaterialTheme.dimens.mediumMargin),
                    logo = Icons.AutoMirrored.Filled.ArrowBack,
                    title = titleHeaderTag,
                    onClickLogo = mockOnClickLogo,
                    menu = mockMenu
                )
            }
            onNodeWithText(titleHeaderTag).assertExists()
            onNodeWithContentDescription(iconHeaderTag).assertExists()
        }
    }

    @Test
    fun header_shouldConfirmClick_whenOnClickLogoIsCalled(){
        var clicked = false
        composeTestRule.apply {
            setContent {
                Header(
                    logo = Icons.AutoMirrored.Filled.ArrowBack,
                    title = titleHeaderTag,
                    onClickLogo = { clicked = true }
                )
            }
            onNodeWithContentDescription(iconHeaderTag).performClick()
            Truth.assertThat(clicked).isTrue()
        }
    }
}