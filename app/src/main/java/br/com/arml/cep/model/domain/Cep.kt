package br.com.arml.cep.model.domain

import br.com.arml.cep.model.exception.CepException

const val CEP_LENGTH = 9
const val MIN_CEP_LENGTH_FOR_SEARCH = 3

@ConsistentCopyVisibility
data class Cep private constructor(val text: String){
    private fun ifCepIsEmpty() {
        if(text.isEmpty()) throw CepException.EmptyCepException()
    }

    private fun ifCepIsSizeInvalid() {
        if(text.length != CEP_LENGTH) throw CepException.SizeCepException()
    }

    private fun ifCepIsInvalid() {
        if(!isValid(text)) throw CepException.IllegalPatternException()
    }

    companion object{
        fun build(input: String): Cep {
            val cep = Cep(input)
            cep.ifCepIsEmpty()
            cep.ifCepIsSizeInvalid()
            cep.ifCepIsInvalid()
            return cep
        }

        fun isValid(input: String): Boolean {
            val regex = Regex("^[0-9]{5}-[0-9]{3}$")
            return regex.matches(input)
        }
    }
}

fun unformatCep(formattedCep: String): String {
    val onlyDigits = formattedCep.filter { it.isDigit() }
    return onlyDigits.filterIndexed { index, _ -> index < 8 }
}

fun updateCepField(
    oldValue: String,
    newValue: String
): String{
    return if(
        unformatCep(newValue).length == 5 &&
        unformatCep(oldValue).length == 5 &&
        oldValue.last() == '-'
    )
        unformatCep(newValue.dropLast(1))
    else
        unformatCep(newValue)
}

fun formattedCep(input: String): String{
    if (input.isEmpty()) return ""
    val builder = StringBuilder()
    input.forEach { char ->
        if(builder.length < 9 && char.isDigit())
            builder.append(char)

        if (builder.length == 5) {
            builder.append('-')
        }
    }
    return builder.toString()
}