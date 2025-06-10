package br.com.arml.cep.model.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Response<out T> {
    data class Success<out T>(val result: T) : Response<T>()
    data class Failure(val exception: Exception) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}

fun <T> asResponse(
    databaseOperation: suspend () -> T
): Flow<Response<T>> = flow {
    emit(Response.Loading)
    try {
        emit(Response.Success(databaseOperation()))
    } catch (e: Exception) {
        emit(Response.Failure(e))
    }
}

fun <T> Flow<T>.toResponseFlow(): Flow<Response<T>> {
    return this
        .map<T, Response<T>> { data -> Response.Success(data) }
        .onStart { emit(Response.Loading) }
        .catch { error ->
            val exception =
                error as? Exception ?: RuntimeException("Flow encountered an error", error)
            emit(Response.Failure(exception))
        }
}