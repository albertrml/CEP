package br.com.arml.cep.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.arml.cep.model.domain.Note

@Entity(
    tableName = "Notes",
    indices = [ Index(value = [ "title" ], unique = true) ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val content: String = ""
)

fun NoteEntity.toModel() = Note.build(
    id = id,
    title = title,
    content = content
)