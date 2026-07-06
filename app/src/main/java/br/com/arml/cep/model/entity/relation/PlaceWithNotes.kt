package br.com.arml.cep.model.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.arml.cep.model.entity.FavoriteEntity

import br.com.arml.cep.model.entity.NoteEntity
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.entity.PlaceEntity
import br.com.arml.cep.model.entity.toModel

data class PlaceWithNotes (
    /*
        O Room entende que o ponto de partida (a entidade base da relação) é o PlaceEntity.
        Cada linha da consulta representará um PlaceEntity + a lista de NoteEntity associadas.
    */
    @Embedded val place: PlaceEntity,

    /*
        Define como ligar o PlaceEntity aos NoteEntity por meio de uma tabela intermediária
        (FavoriteEntity).
     */
    @Relation(
        entity = NoteEntity::class, // destino da relação (a tabela note).
        parentColumn = "id",        // chave da tabela place.
        entityColumn = "id",        // chave da tabela note.
        /*
            SELECT *
            FROM places AS P
            JOIN favorites AS F ON P.zipcode = F.zipcode
            JOIN notes AS N ON F.id_note = N.id;

            ou

            1. O Room pega o zipcode de cada PlaceEntity;
            2. Procura todos os registros na tabela favorites que tenham esse zipcode;
            3. Para cada favorite, busca a nota (NoteEntity) cujo id = id_note;
            4. E finalmente retorna um objeto composto PlaceWithNotes.
        */
        associateBy = Junction(
            value = FavoriteEntity::class,
            parentColumn = "id_place",
            entityColumn = "id_note"
        )
    )
    val notes: List<NoteEntity>
)

fun PlaceWithNotes.toModel(): Place {
    val notes = notes.map { it.toModel() }
    return place.toModel(notes)
}