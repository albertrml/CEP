package br.com.arml.cep.ui.component

import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.mock.mockLogEntries
import br.com.arml.cep.ui.screen.component.common.CepFilter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var searchField: String
    private lateinit var searchButton: String
    private lateinit var query: String

    private val mockOnFilterByCep: (String) -> Unit = mockk(relaxed = true)
    private val mockOnFilterByInitialDate: (Long) -> Unit = mockk(relaxed = true)
    private val mockOnFilterByPeriod: (Long, Long) -> Unit = mockk(relaxed = true)
    private val mockOnFilterByTitle: (String) -> Unit = mockk(relaxed = true)

    private val initialDate: Long = mockLogEntries[3].timestamp.time
    private val finalDate: Long = mockLogEntries[7].timestamp.time

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext.apply {
            searchField = getString(R.string.testTag_cepFilter_searchField)
            searchButton = getString(R.string.testTag_cepFilter_searchButton)
        }

        composeTestRule.setContent { CepFilter(onFilterByCep = mockOnFilterByCep) }
    }

    @Test
    fun cepFilter_validCepEnteredAndSearchClicked_invokesOnFilterByCepAndShowsFormattedCep() {
        query = "1111111123"
        composeTestRule.onNodeWithTag(searchField).apply {
            performTextInput(query)
            assert(hasText(Cep.build(query).toFormattedCep()))
        }
        composeTestRule.onNodeWithTag(searchButton).performClick()
        verify { mockOnFilterByCep(query) }
    }

    @Test
    fun whenSearchButtonClicked_withInvalidCep_thenDoesNotCallOnFilterByCepAndDisplaysErrorMessageOrOriginalInput(){
        query = "1111111"
        composeTestRule.onNodeWithTag(searchField).apply {
            performTextInput(query)
            assert(hasText(Cep.build(query).toFormattedCep()))
        }
        composeTestRule.onNodeWithTag(searchButton).performClick()
        verify { mockOnFilterByCep(query) }
    }
}