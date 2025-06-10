package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.LogEntry
import java.sql.Timestamp

const val BASE_TIMESTAMP = 1749100000000L

val mockLogEntry = List<LogEntry>(15){ i ->
    LogEntry(
        cep = Cep.build("${i%10}".repeat(8)),
        timestamp = Timestamp(BASE_TIMESTAMP + 1000000*i)
    )
}