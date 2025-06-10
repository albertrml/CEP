package br.com.arml.cep.model.mock

import br.com.arml.cep.model.domain.Address
import br.com.arml.cep.model.domain.Cep
import br.com.arml.cep.model.entity.CepSearchEntry
import br.com.arml.cep.model.domain.Favorite
import br.com.arml.cep.model.domain.Note
import java.sql.Timestamp

//const val BASE_TIMESTAMP = 1749100000000L

val mockCepSearchEntry = List<CepSearchEntry>(10){ i ->
    CepSearchEntry(
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
        note = if(i<=4) null else Note.build("Título $i", "Conteúdo $i"),
        timestamp = Timestamp(BASE_TIMESTAMP + 10000000*i)
    )
}
