package br.com.arml.cep.ui.favorite

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.mock.mockFavoritePlaces
import br.com.arml.cep.ui.screen.component.favorite.FavoriteDetailPaneComponent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteDetailsPaneTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val mockFavorite: Pair<Address, Note?> = mockFavoritePlaces.first().run {
        address to notes.first()
    }

    private lateinit var favoriteScreenComponent: String
    private lateinit var favoriteScreenHeader: String
    private lateinit var favoriteScreenContent: String
    private lateinit var favoriteScreenNavigateToExtraButton: String
    private lateinit var favoriteScreenNavigateToListButton: String

    private val onNavigateBackToList: () -> Unit = mockk(relaxed = true)
    private val onEditNote: (Note) -> Unit = mockk(relaxed = true)
    private val onCreateNote: (Cep, Note) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
            favoriteScreenComponent = getString(R.string.testTag_favoriteDetails_component)
            favoriteScreenHeader = getString(R.string.testTag_favoriteDetails_header)
            favoriteScreenContent = getString(R.string.testTag_favoriteDetails_content)
            favoriteScreenNavigateToExtraButton = getString(R.string.testTag_favoriteDetails_button)
            favoriteScreenNavigateToListButton = getString(R.string.headerContent_iconButton_testTag)
        }

        displayFavoriteDetailComponent()
    }

    fun displayFavoriteDetailComponent(favorite: Pair<Address, Note?> = mockFavorite) {
        composeTestRule.setContent {
            FavoriteDetailPaneComponent(
                modifier = Modifier.testTag(favoriteScreenComponent),
                favorite = favorite,
                onNavigateBackToList = onNavigateBackToList,
                onEditNote = onEditNote,
                onCreateNote = onCreateNote
            )
        }
    }

    @Test
    fun shouldDisplayAllComponents_whenFavoriteScreenIsCalled() {
        composeTestRule.apply {
            onNodeWithTag(favoriteScreenComponent).assertExists()
            onNodeWithTag(favoriteScreenHeader).assertExists()
            onNodeWithTag(favoriteScreenContent).assertExists()
            onNodeWithTag(favoriteScreenNavigateToExtraButton).assertExists()
        }
    }

    @Test
    fun shouldNavigateBackToList_whenHeaderIconIsClicked() {
        composeTestRule.onNodeWithTag(favoriteScreenNavigateToListButton).performClick()
        verify { onNavigateBackToList() }
    }

    /*@Test
    fun shouldNavigateToExtra_whenButtonIsClicked() {
        composeTestRule.onNodeWithTag(favoriteScreenNavigateToExtraButton).performClick()
        verify { onNavigateToExtra() }
    }*/
}