package br.com.arml.cep.ui.component.common.address

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.common.address.AddressField
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class AddressFieldTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var addressFieldComponent: String
    private lateinit var addressFieldTitle: String
    private lateinit var addressFieldValue: String

    @Before
    fun setup(){
        composeTestRule.apply{
            InstrumentationRegistry.getInstrumentation().targetContext.apply {
                addressFieldComponent = getString(R.string.addressField_component_testTag)
                addressFieldTitle = getString(R.string.addressField_title_testTag)
                addressFieldValue = getString(R.string.addressField_value_testTag)
            }
        }
    }

    fun addressFieldCompose(title: String, value: String){
        composeTestRule.setContent { AddressField(title = title,value = value) }
    }

    @Test
    fun addressField_shouldDisplayTitleAndValue(){
        val title = "title"
        val value = "value"
        addressFieldCompose(title, value)
        composeTestRule.apply{
            onNodeWithTag(addressFieldComponent)
                .assertExists()

            onNodeWithTag(addressFieldTitle)
                .assertExists()
                .assertTextEquals(title)

            onNodeWithTag(addressFieldValue)
                .assertExists()
                .assertTextEquals(value)
        }
    }
}