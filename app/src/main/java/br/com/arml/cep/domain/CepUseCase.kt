package br.com.arml.cep.domain

import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.model.entity.CEP
import br.com.arml.cep.model.repository.CepRepository
import br.com.arml.cep.util.exception.CepException
import br.com.arml.cep.util.type.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CepUseCase @Inject constructor(
    private val repository: CepRepository
){
    fun searchCep(code: String): Flow<Response<Address>> {
        return try {
            val cep = CEP.build(code)
            repository.getAddressByCep(cep)
        } catch (e: CepException){
            flowOf(Response.Failure(e))
        }
    }
}