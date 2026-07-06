package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Log
import java.sql.Timestamp

const val BASE_TIMESTAMP = 1749100000000L

val mockLogEntries = List<Log>(14){ i ->
    Log(
        cep = mockCep(i),
        timestamp = getMockDate(i)
    )
}

fun getMockDate(index: Int = 0) = Timestamp(BASE_TIMESTAMP + 50000000*index)