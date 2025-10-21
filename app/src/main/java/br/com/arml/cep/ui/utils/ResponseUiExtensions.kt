package br.com.arml.cep.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import br.com.arml.cep.model.domain.Response
import kotlinx.coroutines.delay

@Composable
fun <T> Response<T>.ShowResults(
    successContent: @Composable (T) -> Unit = {},
    loadingContent: @Composable () -> Unit = {},
    failureContent: @Composable (Exception) -> Unit = {},
    delay: Long = 500
) {
    LaunchedEffect(Unit) { delay(delay) }
    when (this) {
        is Response.Success -> { successContent(this.result) }
        is Response.Loading -> { loadingContent() }
        is Response.Failure -> { failureContent(this.exception) }
    }
}