package br.com.arml.cep.model.repository

import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.model.entity.Cep
import br.com.arml.cep.model.source.CepApiService
import br.com.arml.cep.util.exception.CepException
import br.com.arml.cep.util.type.Response
import br.com.arml.cep.util.type.onResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CepRepository @Inject constructor(
    private val service: CepApiService
){
    fun getAddressByCep(cep: Cep): Flow<Response<Address>> = onResponse {
        val cepDAO = service.getAddressByCep(cep.text)
        if (cepDAO.erro == "true") throw CepException.NotFoundCepException()
        cepDAO.toAddress()
    }
}

