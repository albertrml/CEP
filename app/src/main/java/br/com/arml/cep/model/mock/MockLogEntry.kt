package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Log
import java.sql.Timestamp

const val BASE_TIMESTAMP = 1749100000000L

val mockLogEntries = List<Log>(15){ i ->
    Log(
        cep = Cep.build("${i%10}".repeat(8)),
        timestamp = getMockDate(i)
    )
}

fun getMockDate(index: Int = 0) = Timestamp(BASE_TIMESTAMP + 1000000*index)