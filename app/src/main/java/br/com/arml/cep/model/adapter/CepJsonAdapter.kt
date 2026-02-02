package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.exception.AdapterException
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class CepJsonAdapter{
    @ToJson
    fun toJson(cep: Cep): String{
        return cep.text
    }

    @FromJson
    fun fromJson(cep: String): Cep{
        if (!Cep.isValid(cep)) throw AdapterException.InputDoesNotMatchCepPatternException()
        return Cep.build(cep)
    }
}