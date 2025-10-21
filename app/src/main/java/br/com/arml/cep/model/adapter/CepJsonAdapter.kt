package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Cep
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class CepJsonAdapter{
    @ToJson
    fun toJson(cep: Cep): String{
        return cep.text
    }

    @FromJson
    fun fromJson(cep: String): Cep{
        return Cep.build(cep)
    }
}