package br.com.arml.cep.model.entity

data class Address (
    val zipCode: String,
    val street: String,
    val complement: String,
    val district: String,
    val city: String,
    val state: String,
    val uf: String,
    val region: String,
    val country: String = "Brasil",
    val ddd: String,
)

