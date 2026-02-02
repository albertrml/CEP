package br.com.arml.cep.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.text.AnnotatedString

fun hasEditableText(expectedText: String): SemanticsMatcher {
    return SemanticsMatcher.expectValue(
        SemanticsProperties.EditableText, AnnotatedString(expectedText)
    )
}