package br.com.arml.cep.model.mock

fun String.mockFormat(times: Int): String {
    return this.repeat(times).substring(0,times)
}