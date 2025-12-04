package br.com.arml.cep.ui.component.address

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.ui.screen.component.common.address.AddressForms
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressFormsTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var addressFormsComponent: String

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            addressFormsComponent = getString(R.string.addressForms_component_testTag)
        }
    }

    fun displayAddressForm(address: Address){
        composeTestRule.setContent { AddressForms(address = address) }
    }

    @Test
    fun shouldDisplayAddressInformation_whenSearchCepSucceeds(){
        val address = mockAddress(1)
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