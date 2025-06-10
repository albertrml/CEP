package br.com.arml.cep.model.converter

import androidx.room.TypeConverter
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.exception.CepException

object CepConverter {
    @TypeConverter
    fun fromCepToString(cep: Cep?): String? {
        return cep?.text
    }

    @TypeConverter
    fun fromStringToCep(cepText: String?): Cep? {
        return cepText?.let {
            try {
                Cep.build(it)
            } catch (capException: CepException) {
                throw capException
            } catch (e: Exception) {
                throw CepException.ConversionRoomException(it, e.message?: "Error Desconhecido")
            }
        }
    }
}