package br.com.arml.cep.model.mock

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.arml.cep.model.domain.Note

val mockNotes = List<Note>(5){
    Note.build(
        id = it.toLong() + 1,
        title = "Title $it",
        content = LoremIpsum(it*10).values.toString()
    )
}

fun generateMockNotes(index: Int, size: Int = 5) = List<Note>(size){
    Note.build(
        id = (it+1).toLong(),
        title = "Title $index: ${it + 1}",
        content = LoremIpsum(index*10).values.toString()
    )
}