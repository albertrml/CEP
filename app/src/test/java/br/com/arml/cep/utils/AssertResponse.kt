package br.com.arml.cep.utils

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
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