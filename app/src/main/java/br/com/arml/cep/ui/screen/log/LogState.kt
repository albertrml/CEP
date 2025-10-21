package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.LogEntry
import br.com.arml.cep.ui.utils.LogFilterOption

data class LogState(
    val fetchEntries: Response<List<LogEntry>> = Response.Loading,
    val deleteLog: Response<Unit> = Response.Loading,
    val filterOperation: LogFilterOption = LogFilterOption.None
)