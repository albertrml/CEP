package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.LogEntry

data class LogState(
    val fetchLog: Response<List<LogEntry>> = Response.Loading,
    val wasFiltered: Boolean = false,
    val deleteLog: Response<Unit> = Response.Loading
)