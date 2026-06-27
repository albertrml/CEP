package br.com.arml.cep.model.utils

import java.text.Normalizer
import kotlin.text.lowercase

private val DIACRITICS_REGEX = "\\p{Mn}+".toRegex()
private val NON_ALPHANUMERIC_REGEX = "[^a-z0-9]".toRegex()
private val WHITESPACE_REGEX = "\\s+".toRegex()

fun String.normalizeForDBSearch(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(DIACRITICS_REGEX, "")
        .lowercase()
        .replace(NON_ALPHANUMERIC_REGEX, "")
        .replace(WHITESPACE_REGEX, " ")
        .trim()

fun String.normalizeForAPISearch(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(DIACRITICS_REGEX, "")
        .lowercase()
        //.replace(NON_ALPHANUMERIC_REGEX, "")
        .replace(WHITESPACE_REGEX, "+")
        .trim()
