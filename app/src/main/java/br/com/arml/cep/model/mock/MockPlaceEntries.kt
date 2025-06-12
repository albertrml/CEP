package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import br.com.arml.cep.model.entity.PlaceEntry

val mockPlaceEntries = List<PlaceEntry>(15){ i ->
    PlaceEntry(
        cep = Cep.build("${i%10}".repeat(8)),
        address = Address(
            zipCode = "${i%10}".repeat(5) + "-" + "${i%10}".repeat(3),
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

val mockFavoritePlaceEntries = List<PlaceEntry>(20){ i ->
    PlaceEntry(
        cep = Cep.build("${i%10}".repeat(8)),
        address = Address(
            zipCode = "${i%10}".repeat(5) + "-" + "${i%10}".repeat(3),
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
        isFavorite = Favorite(true),
        note = Note.build("Título $i", "Conteúdo $i")
    )
}