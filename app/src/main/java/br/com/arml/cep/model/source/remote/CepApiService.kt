package br.com.arml.cep.model.source.remote

import br.com.arml.cep.model.dto.AddressDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CepApiService{
    @GET("ws/{cep}/json")
    suspend fun getAddressByCep(@Path("cep") cep: String): AddressDTO
}