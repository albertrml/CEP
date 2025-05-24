package br.com.arml.cep.util.type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class Response <out T>{
    data class Success <out T>(val result: T): Response<T>()
    data class Failure (val exception: Exception): Response<Nothing>()
    data object Loading: Response<Nothing>()
}

fun <T> onResponse(
    databaseOperation: suspend () -> T
): Flow<Response<T>> = flow {
    emit(Response.Loading)
    try {
        emit(Response.Success(databaseOperation()))
    } catch (e: Exception) {
        emit(Response.Failure(e))
    }
}

@Composable
fun <T> Response<T>.ShowResults(
    successContent: @Composable (T) -> Unit = {},
    loadingContent: @Composable () -> Unit = {},
    failureContent: @Composable (Exception) -> Unit = {},
    delay: Long = 500
) {
    LaunchedEffect(Unit) {
        delay(delay)
    }
    when (this) {
        is Response.Success -> {
            successContent(this.result)
        }

        is Response.Loading -> {
            loadingContent()
        }

        is Response.Failure -> {
            failureContent(this.exception)
        }
    }
}