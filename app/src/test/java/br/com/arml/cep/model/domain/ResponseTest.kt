package br.com.arml.cep.model.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResponseTest {

    @Test
    fun `Response Success holds result correctly`() {
        val data = "Test Data"
        val response = Response.Success(data)
        assertEquals(data, response.result)
    }

    @Test
    fun `Response Failure holds exception correctly`() {
        val exception = RuntimeException("Test Exception")
        val response = Response.Failure(exception)
        assertEquals(exception, response.exception)
    }

    @Test
    fun `Response Loading is a singleton object`() {
        val loading1 = Response.Loading
        val loading2 = Response.Loading
        assertEquals(loading1,loading2)
    }

    @Test
    fun `asResponse emits Loading then Success when operation is successful`() = runTest {
        val expectedData = "Test Data"
        val operation: suspend () -> String = { expectedData }

        val flow = asResponse(operation)
        val results = flow.toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is Response.Loading)
        assertTrue(results[1] is Response.Success)
        assertEquals(expectedData, (results[1] as Response.Success).result)
    }

    @Test
    fun `asResponse should emit Loading then Failure when operation throws an exception`() = runTest{
        val expectedException = Exception("Test Exception")
        val mockOperation: suspend () -> String = mockk()
        coEvery{ mockOperation() } throws expectedException

        val flow = asResponse(mockOperation)
        val results = flow.toList()

        assertEquals(2,results.size)
        assertEquals(Response.Loading,results[0])
        assertEquals(Response.Failure(expectedException),results[1])
        assertEquals(expectedException, (results[1] as Response.Failure).exception)
    }

    @Test
    fun `toResponseFlow should emit Loading then Success when operation is successful`() = runTest{
        val expectedData = "Test Data"
        val expectedFlow = flowOf(expectedData)
        val flow = expectedFlow.toResponseFlow()
        val results = flow.toList()
        assertEquals(2,results.size)
        assertEquals(Response.Loading,results[0])
        assertEquals(Response.Success("Test Data"),results[1])
        assertEquals(expectedData,(results[1] as Response.Success).result)
    }

    @Test
    fun `toResponseFlow should emit Loading then Failure when operation is unsuccessful`() = runTest{
        val expectedException = Exception("Test Exception")
        val errorFlow = flow<String> { throw expectedException }
        val flow = errorFlow.toResponseFlow()
        val results = flow.toList()
        assertEquals(2,results.size)
        assertEquals(Response.Loading,results[0])
        assertEquals(Response.Failure(expectedException),results[1])
        assertEquals(expectedException,(results[1] as Response.Failure).exception)
    }
}