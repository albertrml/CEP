package br.com.arml.cep.model.adapter

import br.com.arml.cep.model.domain.Note
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class NoteJson(val title: String, val content: String)

class NoteJsonAdapter {

    @ToJson
    fun toJson(note: Note): NoteJson {
        return NoteJson(note.title, note.content)
    }

    @FromJson
    fun fromJson(note: NoteJson): Note {
        return Note.build(note.title, note.content)
    }

}