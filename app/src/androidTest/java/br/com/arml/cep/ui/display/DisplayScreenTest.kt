package br.com.arml.cep.ui.display

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.ui.screen.component.display.DisplayScreen
import br.com.arml.cep.ui.theme.CEPTheme
import br.com.arml.cep.util.exception.CepException
import br.com.arml.cep.util.type.Response
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DisplayScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnBackPress: () -> Unit = mockk()

    private lateinit var displayAddressTitle: String
    private lateinit var mockAddressData: Address
    private lateinit var backButton: String

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        displayAddressTitle = context.getString(R.string.display_address_title)
        backButton = context.getString(R.string.icon_button_tag)
        mockAddressData = mockAddress // Use seu mockAddress
        every { mockOnBackPress() } answers {
            println("mockOnBackPress CALLED")
        }
    }

    private fun setDisplayScreenContent(response: Response<Address>) {
        composeTestRule.setContent {
            CEPTheme {
                DisplayScreen(
                    modifier = Modifier.fillMaxSize(),
                    response = response,
                    onBackPress = mockOnBackPress
                )
            }
        }
    }

    @Test
    fun displayScreen_header_showsCorrectTitle() {
        setDisplayScreenContent(Response.Loading) // Pode ser qualquer estado para testar o header
        composeTestRule.onNodeWithText(displayAddressTitle).assertIsDisplayed()
    }

    @Test
    fun displayScreen_header_callsOnBackPress_whenBackButtonClicked() {
        setDisplayScreenContent(Response.Loading)
        composeTestRule.onNodeWithTag(backButton)
            .performClick()
        verify { mockOnBackPress() }
    }

    @Test
    fun displayScreen_whenLoading_showsCircularProgressIndicator() {
        setDisplayScreenContent(Response.Loading)
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun displayScreen_whenSuccess_showsAddressScreenWithCorrectData() {
        setDisplayScreenContent(Response.Success(mockAddressData))
        composeTestRule.onNodeWithText(mockAddressData.zipCode).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.street).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.complement).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.district).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.city).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.state).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.uf).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.region).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.country).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockAddressData.ddd).assertIsDisplayed()
    }

    @Test
    fun displayScreen_shouldShowNotFoundCepErrorMessage_whenFailureWithNotFoundCepException() {
        val errorMessage = CepException.NotFoundCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }

    @Test
    fun displayScreen_shouldShowInputCepExceptionMessage_whenFailureWithInputCepException() {
        val errorMessage = CepException.InputCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }

    @Test
    fun displayScreen_shouldShowSizeCepExceptionMessage_whenFailureWithSizeCepException() {
        val errorMessage = CepException.SizeCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }

    @Test
    fun displayScreen_shouldShowEmptyCepExceptionMessage_whenFailureWithEmptyCepException() {
        val errorMessage = CepException.EmptyCepException()
        setDisplayScreenContent(Response.Failure(errorMessage))
        composeTestRule.onNodeWithText(errorMessage.message).assertIsDisplayed()
    }
}