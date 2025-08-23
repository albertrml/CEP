package br.com.arml.cep.ui.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.search.AddressForms
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddressFormsTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var addressFormsComponent: String

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            addressFormsComponent = getString(R.string.testTag_addressForms_component)
        }
    }

    fun displayAddressForm(address: Address){
        composeTestRule.setContent {
            AddressForms(
                modifier = Modifier.testTag(addressFormsComponent),
                address = address
            )
        }
    }

    @Test
    fun shouldDisplayAddressInformation_whenSearchCepSucceeds(){
        val address = mockPlaceEntries.first().address
        displayAddressForm(address)
        composeTestRule.apply {
            onNodeWithTag(addressFormsComponent).assertIsDisplayed()
            address.apply {
                onNodeWithText(zipCode).assertIsDisplayed()
                onNodeWithText(street).assertIsDisplayed()
                onNodeWithText(complement).assertIsDisplayed()
                onNodeWithText(district).assertIsDisplayed()
                onNodeWithText(city).assertIsDisplayed()
                onNodeWithText(state).assertIsDisplayed()
                onNodeWithText(uf).assertIsDisplayed()
                onNodeWithText(region).assertIsDisplayed()
                onNodeWithText(country).assertIsDisplayed()
                onNodeWithText(ddd).assertIsDisplayed()
            }
        }
    }
}