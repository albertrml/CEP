package br.com.arml.cep.model.source

import br.com.arml.cep.model.entity.CepDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CepApiService{
    @GET("ws/{cep}/json")
    suspend fun getAddressByCep(@Path("cep") cep: String): CepDTO
}