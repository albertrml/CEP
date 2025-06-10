package br.com.arml.cep.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.cep.search.CepField
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CepFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private var lastQuery: String? = null
    private fun onQueryChangeCallback(query: String) { lastQuery = query }

    @Test
    fun cepField_initialState_displaysPlaceholder() {
        // Obter string do placeholder de dentro de um Composable no teste
        var placeholderText = ""
        composeTestRule.setContent {
            placeholderText = stringResource(R.string.search_cep_hint)
            MaterialTheme { // Envolver com MaterialTheme se seu Composable usa atributos dele
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CepField(onQueryChange = ::onQueryChangeCallback)
                }
            }
        }
    }

    @Test
    fun cepFiels_should_shows_cep_when_lastQuery_is_12345678(){
        var labelText = ""
        composeTestRule.setContent {
            labelText = stringResource(R.string.search_cep_field_name) // Use o texto do label para encontrar o campo
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CepField(onQueryChange = ::onQueryChangeCallback)
                }
            }
        }
        val inputNode = composeTestRule.onNodeWithText(labelText)
        inputNode.performClick()
        inputNode.performTextInput("12345678")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-678").assertExists()
    }

    @Test
    fun cepFiels_should_shows_cep_when_lastQuery_is_a12n34n5oi67ja89(){
        var labelText = ""
        composeTestRule.setContent {
            labelText = stringResource(R.string.search_cep_field_name) // Use o texto do label para encontrar o campo
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CepField(onQueryChange = ::onQueryChangeCallback)
                }
            }
        }
        val inputNode = composeTestRule.onNodeWithText(labelText)
        inputNode.performClick()
        inputNode.performTextInput("a12n34n5oi67ja89")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-678").assertExists()
    }

    @Test
    fun cepFiels_should_shows_cep_when_lastQuery_is_123456(){
        var labelText = ""
        composeTestRule.setContent {
            labelText = stringResource(R.string.search_cep_field_name) // Use o texto do label para encontrar o campo
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CepField(onQueryChange = ::onQueryChangeCallback)
                }
            }
        }
        val inputNode = composeTestRule.onNodeWithText(labelText)
        inputNode.performClick()
        inputNode.performTextInput("123456")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-6").assertExists()
    }

    @Test
    fun cepField_whenDeletingCharacters_formatsCorrectlyAndCallsOnQueryChange(){
        var labelText = ""
        composeTestRule.setContent {
            labelText = stringResource(R.string.search_cep_field_name) // Use o texto do label para encontrar o campo
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CepField(onQueryChange = ::onQueryChangeCallback)
                }
            }
        }
        val inputNode = composeTestRule.onNodeWithText(labelText)
        inputNode.performClick()
        inputNode.performTextInput("12345678")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-678").assertExists()

        inputNode.performTextClearance()
        inputNode.performTextInput("1234567")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-67").assertExists()

        inputNode.performTextClearance()
        inputNode.performTextInput("123456")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-6").assertExists()

        inputNode.performTextClearance()
        inputNode.performTextInput("12345")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("12345-").assertExists()

        inputNode.performTextClearance()
        inputNode.performTextInput("1234")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1234").assertExists()

    }
}