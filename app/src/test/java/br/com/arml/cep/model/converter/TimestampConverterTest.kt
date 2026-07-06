package br.com.arml.cep.model.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.Timestamp

class TimestampConverterTest {
    @Test
    fun `should convert Timestamp to Long and back`(){
        val expectedTimestamp = Timestamp(System.currentTimeMillis())
        val expectedTimestampLong = expectedTimestamp.time
        val actualTimestampLong = TimestampConverter.fromTimestampToLong(expectedTimestamp)
        val actualTimestamp = TimestampConverter.fromLongToTimestamp(expectedTimestampLong)
        val actualNullTimestamp = TimestampConverter.fromLongToTimestamp(null)
        val actualNullTimestampLong = TimestampConverter.fromTimestampToLong(null)
        assertEquals(expectedTimestampLong, actualTimestampLong)
        assertEquals(expectedTimestamp, actualTimestamp)
        assertEquals(null, actualNullTimestamp)
        assertEquals(null, actualNullTimestampLong)
    }
}