package br.com.arml.cep.utils

import br.com.arml.core.response.Response
import br.com.arml.core.response.Response.Loading
import br.com.arml.core.response.Response.Success
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

fun <T> List<Response<T>>.assertFlowSuccess(
    assertContent: (T) -> Unit
) {
    assertEquals("Expected 2 responses (Loading, then Success)", 2, this.size)
    assertTrue("First response should be Loading", this[0] is Loading)

    when (val response = this[1]) {
        is Success -> assertContent(response.result)
        else -> fail("Second response should be Success, but was $response")
    }
}

fun <T> List<Response<T>>.assertFlowFailure(
    assertException: (Throwable) -> Unit
) {
    assertEquals("Expected 2 responses (Loading, then Failure)", 2, this.size)
    assertTrue("First response should be Loading", this[0] is Loading)

    when (val response = this[1]) {
        is Response.Failure -> assertException(response.exception)
        else -> fail("Second response should be Failure, but was $response")
    }
}

inline fun <T> mockAnswer(
    crossinline f: suspend () -> T,
    data: T,
    exception: Exception? = null,
){
    exception
        ?.let { coEvery { f() } throws it }
        ?: (coEvery { f() } coAnswers { data })
}

inline fun <T> mockFlowAnswer(
    crossinline f: () -> Flow<T>,
    data: T,
    exception: Exception? = null,
) {
    every { f() } returns if (exception != null) {
        flow { throw exception }
    } else {
        flowOf(data)
    }
}