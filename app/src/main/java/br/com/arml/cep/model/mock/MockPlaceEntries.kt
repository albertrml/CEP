package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.entity.PlaceEntry

val mockPlaceEntries = List<PlaceEntry>(10){ i ->
    PlaceEntry(
        cep = Cep.build("$i".repeat(8)),
        address = Address(
            zipCode = "$i".repeat(5) + "-" + "$i".repeat(3),
            street = "Rua $i",
            complement = "Complemento $i",
            district = "Bairro $i",
            city = "Cidade $i",
            state = "Estado $i",
            uf = "BR",
            region = "Região $i",
            country = "Brasil",
            ddd = "$i".repeat(2),
        ),
        isFavorite = Favorite(i > 4),
        note = if(i<=4) null else Note.build("Título $i", "Conteúdo $i")
    )
}