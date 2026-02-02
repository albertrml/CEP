package br.com.arml.cep.utils

import br.com.arml.cep.model.domain.Place

sealed class OperationOnSuccess<T>(val result: T) {
    class AddNoteToFavorite(result: Unit = Unit) : OperationOnSuccess<Unit>(result)
    class GetFavoritesByZipcode(result: List<Place>) : OperationOnSuccess<List<Place>>(result)
    class GetFavoritesByTitle(result: List<Place>) : OperationOnSuccess<List<Place>>(result)
    class UpdateNoteFromFavorite(result: Unit = Unit) : OperationOnSuccess<Unit>(result)
    class DeleteFromFavorite(result: Unit = Unit) : OperationOnSuccess<Unit>(result)
    class DeleteNoteFromFavorite(result: Unit = Unit) : OperationOnSuccess<Unit>(result)
    class ExportFavorites(result: List<Place>) : OperationOnSuccess<List<Place>>(result)
    class ImportFavorites(result: Unit = Unit) : OperationOnSuccess<Unit>(result)
}

sealed class OperationOnFailure(val exception: Exception) {
    class AddNoteToFavorite(exception: Exception) : OperationOnFailure(exception)
    class GetFavoritesByZipcode(exception: Exception) : OperationOnFailure(exception)
    class GetFavoritesByTitle(exception: Exception) : OperationOnFailure(exception)
    class UpdateNoteFromFavorite(exception: Exception) : OperationOnFailure(exception)
    class DeleteFromFavorite(exception: Exception) : OperationOnFailure(exception)
    class DeleteNoteFromFavorite(exception: Exception) : OperationOnFailure(exception)
    class ExportFavorites(exception: Exception) : OperationOnFailure(exception)
    class ImportFavorites(exception: Exception) : OperationOnFailure(exception)
}