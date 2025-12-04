package br.com.arml.cep.ui.component.filter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.common.filter.TitleFilter
import br.com.arml.cep.utils.hasEditableText
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TitleFilterTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val mockPlace = mockFavoritePlaces.first()
    private lateinit var titleFilterComposable: String
    private lateinit var titleFilterField: String
    private lateinit var titleFilterButton: String
    private val mockOnTitleFilter: (String) -> Unit = mockk()
    private val invalidQueryByMinLength = "A".repeat(MIN_TITLE_LENGTH - 1)
    private val invalidQueryByMaxLength = "A".repeat(MAX_TITLE_LENGTH + 1)
    private val validQuery = mockPlace
        .notes.first()
        .title.substring(0..<MIN_TITLE_LENGTH)

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            titleFilterComposable = getString(R.string.titleFilter_component_testTag)
            titleFilterField = getString(R.string.titleFilter_searchField_testTag)
            titleFilterButton = getString(R.string.titleFilter_filterButton_testTag)
        }

        every { mockOnTitleFilter(any()) } answers { println("OnTitleFilter CALLED") }

        mockTitleFilterComposable()
    }

    fun mockTitleFilterComposable() {
        composeTestRule.setContent {
            TitleFilter(
                modifier = Modifier.fillMaxSize(),
                nameFilter = "Teste",
                maxSize = MAX_TITLE_LENGTH,
                onFilterByTitle = {}
            )
        }
    }

    @Test
    fun titleFilter_shouldDisplayComposableTitleAndButtonNode() {
        composeTestRule.apply {
            onNodeWithTag(titleFilterComposable).assertExists()
            onNodeWithTag(titleFilterField).assertExists()
            onNodeWithTag(titleFilterButton).assertExists()
        }
    }

    @Test
    fun titleFilter_shouldUnableFilterButton_whenQueryDoesNotAttendMinSize() {
        composeTestRule.apply{
            onNodeWithTag(titleFilterField).performTextInput(invalidQueryByMinLength)
            onNodeWithTag(titleFilterButton).assertIsNotEnabled()
        }
    }

    @Test
    fun titleFilter_shouldEnableFilterButtonButDoesNotExceedMaxSize_whenQueryDoesNotAttendMaxSize() {
        val expectedQuery = "A".repeat(MAX_TITLE_LENGTH)
        composeTestRule.apply{
            onNodeWithTag(titleFilterField).apply {
                performTextInput(invalidQueryByMaxLength)
                assert(hasEditableText(expectedQuery))
            }
        }
    }

    @Test
    fun shouldFilter_whenQueryIsValid() {
        composeTestRule.apply {
            onNodeWithTag(titleFilterField).performTextInput(validQuery)
            onNodeWithTag(titleFilterButton).assertIsEnabled()
        }
    }
}