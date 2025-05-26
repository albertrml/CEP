package br.com.arml.cep.model.entity

import br.com.arml.cep.util.exception.CepException

@ConsistentCopyVisibility
data class Cep private constructor(
    val text: String
){

    companion object{
        fun build(input: String): Cep {
            validate(input)
            return Cep(input)
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
                if(builder.length < 9 && char.isDigit())
                    builder.append(char)

                if (builder.length == 5) {
                    builder.append('-')
                }
            }
            return builder.toString()
        }

        fun unFormat(formattedCep: String): String {
            val onlyDigits = formattedCep.filter { it.isDigit() }
            return onlyDigits.filterIndexed { index, _ -> index < 8 }
        }
    }
}

fun updateCepField(
    oldValue: String,
    newValue: String
): String{
    return if(
        Cep.unFormat(newValue).length == 5  &&
        Cep.unFormat(oldValue).length == 5 &&
        oldValue.last() == '-')
        Cep.unFormat(newValue.dropLast(1))
    else
        Cep.unFormat(newValue)
}