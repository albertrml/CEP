package br.com.arml.cep.ui.component.cache.listpane

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.mock.mockUnfavoritePlaces
import br.com.arml.cep.ui.screen.component.cache.listpane.CacheListPaneOnSuccess
import br.com.arml.cep.ui.utils.PlaceFilterOption
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class CacheListPaneOnSuccessTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    private val filterTag = ctx.getString(
        R.string.cacheListPaneOnSuccess_placeFilterComponent_testTag
    )
    private val deleteAllTag = ctx.getString(
        R.string.cacheListPaneOnSuccess_deleteAllComponent_testTag
    )
    private val cachePlaceListTag = ctx.getString(
        R.string.cacheListPaneOnSuccess_cachePlaceList_testTag
    )
    private val searchFieldTag = ctx.getString(
        R.string.searchCepField_component_testTag
    )
    private val cepFilterButtonTag = ctx.getString(
        R.string.cepFilter_filterButton_testTag
    )

    @Test
    fun cacheListPaneOnSuccess_shouldDisplayFilterDeleteAllAndCachePlaceList(){
        composeTestRule.apply{
            setContent {
                CacheListPaneOnSuccess(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onCepFilter = {},
                    onClearFilter = {},
                    onNavigateToDetail = {},
                    onDeleteAllCache = {}
                )
            }
            onNodeWithTag(filterTag).assertIsDisplayed()
            onNodeWithTag(deleteAllTag).assertIsDisplayed()
            onNodeWithTag(cachePlaceListTag).assertIsDisplayed()
        }
    }

    @Test
    fun cacheListPaneOnSuccess_shouldInvokesOnCepFilterCallback_whenFilterButtonIsSelected(){
        val expectedQuery = "111"
        val mockOnCepFilter: (String) -> Unit = mockk(relaxed = true)
        val isCheckBox = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox)
        val checkBoxName = hasTextExactly(PlaceFilterOption.ByCep.name)
        composeTestRule.apply {
            setContent {
                CacheListPaneOnSuccess(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onCepFilter = { query -> mockOnCepFilter(query) },
                    onClearFilter = {},
                    onNavigateToDetail = {},
                    onDeleteAllCache = {}
                )
            }
            onNode(checkBoxName and isCheckBox).performClick()
            onNodeWithTag(searchFieldTag).performTextInput(expectedQuery)
            onNodeWithTag(cepFilterButtonTag).performClick()
            verify (exactly = 1) { mockOnCepFilter(expectedQuery) }
        }
    }

    @Test
    fun cacheListPaneOnSuccess_shouldInvokesOnClearFilter_whenNoneFilterIsSelected(){
        val mockOnClearFilter: () -> Unit = mockk(relaxed = true)
        val isCheckBox = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox)
        val cepCheckBoxName = hasTextExactly(PlaceFilterOption.ByCep.name)
        val noneCheckBoxName = hasTextExactly(PlaceFilterOption.None.name)
        composeTestRule.apply {
            setContent {
                CacheListPaneOnSuccess(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onCepFilter = {},
                    onClearFilter = mockOnClearFilter,
                    onNavigateToDetail = {},
                    onDeleteAllCache = {}
                )
            }
            onNode(cepCheckBoxName and isCheckBox).performClick()
            onNode(noneCheckBoxName and isCheckBox).performClick()
            verify (exactly = 1) { mockOnClearFilter() }
        }
    }

    @Test
    fun cacheListPaneOnSuccess_shouldInvokesOnDeletePlace_whenPlaceDeleteButtonIsClicked(){
        val expectedPlace = mockUnfavoritePlaces.first()
        val mockOnDeletePlace: (Place) -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                CacheListPaneOnSuccess(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = { place -> mockOnDeletePlace(place) },
                    onCepFilter = {},
                    onClearFilter = {},
                    onNavigateToDetail = {},
                    onDeleteAllCache = {}
                )
            }
            val deleteButtonTag = ctx.getString(
                R.string.cachePlaceElement_deleteIcon_testTag,
                expectedPlace.address.zipCode
            )
            onNodeWithTag(deleteButtonTag).performClick()
            verify (exactly = 1) { mockOnDeletePlace(expectedPlace) }
        }
    }

    @Test
    fun cacheListPaneOnSuccess_shouldInvokesOnNavigateToDetailCallback_whenPlaceElementIsClicked(){
        val expectedPlace = mockUnfavoritePlaces.first()
        val onNavigateToDetail: (Place) -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                CacheListPaneOnSuccess(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onCepFilter = {},
                    onClearFilter = {},
                    onNavigateToDetail = { place -> onNavigateToDetail(place) },
                    onDeleteAllCache = {}
                )
            }
            val placeElementTag = ctx.getString(
                R.string.cachePlaceElement_component_testTag,
                expectedPlace.toString()
            )
            onNodeWithTag(placeElementTag).performClick()
            verify (exactly = 1) { onNavigateToDetail(expectedPlace) }
        }
    }

    @Test
    fun cacheListPaneOnSuccess_shouldInvokesOnDeleteAllCache_whenDeleteAllButtonIsClicked(){
        val deleteAllButtonTag = ctx.getString(
            R.string.deleteAllComponent_button_testTag
        )
        val confirmButtonText = ctx.getString(
            R.string.cepAlertdialog_confirmButton_text
        )
        val mockOnDeleteAll: () -> Unit = mockk(relaxed = true)
        composeTestRule.apply {
            setContent {
                CacheListPaneOnSuccess(
                    places = mockUnfavoritePlaces,
                    onDeletePlace = {},
                    onCepFilter = {},
                    onClearFilter = {},
                    onNavigateToDetail = {},
                    onDeleteAllCache = mockOnDeleteAll
                )
            }
            onNodeWithTag(deleteAllButtonTag).performClick()
            onNodeWithText(confirmButtonText).performClick()
            verify (exactly = 1) { mockOnDeleteAll() }
        }
    }
}