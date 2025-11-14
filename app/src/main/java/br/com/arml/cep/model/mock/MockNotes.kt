package br.com.arml.cep.model.mock

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.arml.cep.model.domain.Note

val mockNotes = List<Note>(5){
    Note.build(
        id = it.toLong(),
        title = "Title $it",
        content = LoremIpsum(it*10).values.toString()
    )
}