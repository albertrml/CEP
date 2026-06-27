package br.com.arml.cep.ui.screen.search

import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.ui.screen.search.SearchEffect.ShowSnackbar
import br.com.arml.cep.ui.screen.search.SearchEvent.OnFavoriteResponse
import br.com.arml.cep.ui.utils.UiText
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SearchReducerTest {
    private val reducer = SearchReducer()
    private val initialState = SearchState()

    @Test
    fun `OnFavoriteResponse Success should emit ShowSnackbar with correct UiText`() {
        val zipcode = "12345-678"
        val event = OnFavoriteResponse(Response.Success(Unit), zipcode)
        
        val (_, effect) = reducer.reduce(initialState, event)

        assertThat(effect).isInstanceOf(ShowSnackbar::class.java)
        val snackbarEffect = effect as ShowSnackbar
        assertThat(snackbarEffect.message).isInstanceOf(UiText.StringResource::class.java)
        val uiText = snackbarEffect.message as UiText.StringResource
        assertThat(uiText.resId).isEqualTo(R.string.search_add_favorite_success)
        assertThat(uiText.args).asList().containsExactly(zipcode)
    }

    @Test
    fun `OnFavoriteResponse Failure should emit ShowSnackbar with failure UiText`() {
        val event = OnFavoriteResponse(Response.Failure(Exception()), "12345-678")
        
        val (_, effect) = reducer.reduce(initialState, event)

        assertThat(effect).isInstanceOf(ShowSnackbar::class.java)
        val snackbarEffect = effect as ShowSnackbar
        assertThat(snackbarEffect.message).isInstanceOf(UiText.StringResource::class.java)
        val uiText = snackbarEffect.message as UiText.StringResource
        assertThat(uiText.resId).isEqualTo(R.string.search_add_favorite_failure)
    }
}