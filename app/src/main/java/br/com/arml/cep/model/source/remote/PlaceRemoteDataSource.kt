package br.com.arml.cep.model.source.remote

import br.com.arml.cep.model.entity.dto.AddressDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceRemoteDataSource{
    @GET("{cep}/json")
    suspend fun getAddressByCep(
        @Path("cep") cep: String
    ): AddressDTO

    @GET("{uf}/{city}/{street}/json")
    suspend fun getAddresses(
        @Path("uf") uf: String,
        @Path("city") city: String,
        @Path("street") street: String
    ): List<AddressDTO>
}