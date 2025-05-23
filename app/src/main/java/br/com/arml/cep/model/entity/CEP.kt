package br.com.arml.cep.model.entity

import br.com.arml.cep.util.exception.CepException

@ConsistentCopyVisibility
data class CEP private constructor(
    val text: String
){

    companion object{
        fun build(input: String): CEP {
            validate(input)
            return CEP(input)
        }

        private fun validate(text: String){
            if(text.isEmpty()) throw CepException.EmptyCepException()
            if(text.any { !it.isDigit() }) throw CepException.InputCepException()
            if(text.length != 8) throw CepException.SizeCepException()
        }

        fun format(digitsOnly: String): String{
            if (digitsOnly.isEmpty()) return ""
            val builder = StringBuilder()
            digitsOnly.forEachIndexed { index, char ->
                builder.append(char)
                if (index == 4 && digitsOnly.length > 5) {
                    builder.append('-')
                }
            }
            return builder.toString()
        }

        fun unFormat(formattedCep: String): String {
            return formattedCep.filter { it.isDigit() }
        }
    }
}