package br.com.arml.cep.ui.screen.log

import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Log
import br.com.arml.cep.ui.common.Reducer

data class LogState(
    val logs: Response<List<Log>> = Response.Loading
): Reducer.ViewState