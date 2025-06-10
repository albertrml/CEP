package br.com.arml.cep.model.domain

data class Favorite(val value: Boolean = false){
    fun markAsFavorite() = copy(value = true)
    fun unmarkAsFavorite() = copy(value = false)
}
