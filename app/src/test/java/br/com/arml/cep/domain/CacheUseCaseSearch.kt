package br.com.arml.cep.domain

import br.com.arml.cep.model.domain.Response.Failure
import br.com.arml.cep.model.domain.Response.Loading
import br.com.arml.cep.model.domain.Response.Success
import br.com.arml.cep.model.mock.mockUnfavoritePlaceEntries
import br.com.arml.cep.model.repository.PlaceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheUseCaseSearch {
    private val repository = mockk<PlaceRepository>()
    private lateinit var useCase: CacheUseCase

    @Before
    fun setup() {
        useCase = CacheUseCase(repository)
    }

    /** Delete All **/
    @Test
    fun deleteAll_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        coEvery {
            repository.deleteAllUnwantedPlaces()
        } answers { flow { emit(Success(Unit)) } }

        useCase.deleteAll().collect { response ->
            when (response) {
                is Success -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun deleteAll_shouldReturnLoading_whenRepositoryIsPerforming() = runTest {
        coEvery {
            repository.deleteAllUnwantedPlaces()
        } returns flow { emit(Loading) }

        useCase.deleteAll().collect { response ->
            when (response) {
                is Loading -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun deleteAll_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        coEvery {
            repository.deleteAllUnwantedPlaces()
        } returns flow {
            emit(Failure(Exception("Error")))
        }

        useCase.deleteAll().collect { response ->
            when (response) {
                is Failure -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    /** Delete Entry **/
    @Test
    fun deleteEntry_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        coEvery {
            repository.deletePlace(any())
        } answers { flow { emit(Success(Unit)) } }

        useCase.deleteEntry(mockUnfavoritePlaceEntries.first()).collect { response ->
            when (response) {
                is Success -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun deleteEntry_shouldReturnLoading_whenRepositoryIsPerforming() = runTest {
        coEvery {
            repository.deletePlace(any())
        } answers { flow { emit(Loading) } }

        useCase.deleteEntry(mockUnfavoritePlaceEntries.first()).collect { response ->
            when (response) {
                is Loading -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun deleteEntry_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        coEvery {
            repository.deletePlace(any())
        } answers { flow { emit(Failure(Exception("Error"))) } }

        useCase.deleteEntry(mockUnfavoritePlaceEntries.first()).collect { response ->
            when (response) {
                is Failure -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    /** Fetch Cache **/
    @Test
    fun fetchCache_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        coEvery {
            repository.getUnwantedPlaces()
        } answers { flow { emit(Success(mockUnfavoritePlaceEntries)) } }

        useCase.fetchCache().collect { response ->
            when (response) {
                is Success -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun fetchCache_shouldReturnLoading_whenRepositoryIsPerforming() = runTest {
        coEvery {
            repository.getUnwantedPlaces()
        } answers { flow { emit(Loading) } }

        useCase.fetchCache().collect { response ->
            when (response) {
                is Loading -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun fetchCache_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        coEvery {
            repository.getUnwantedPlaces()
        } answers { flow { emit(Failure(Exception("Error"))) } }

        useCase.fetchCache().collect { response ->
            when (response) {
                is Failure -> {
                    assertTrue(true)
                }

                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    /** Filter By Cep **/
    @Test
    fun filterByCep_shouldReturnSuccessAndNotEmpty_whenRepositoryPerformsSuccessfully() = runTest {
        coEvery {
            repository.getUnwantedPlacesByCepAndUnwanted(any())
        } answers {
            flow {
                val result = mockUnfavoritePlaceEntries.filter {
                    it.cep == args[0]
                }

                emit(Success(result))
            }
        }

        val cepText = mockUnfavoritePlaceEntries.first().cep.text
        useCase.filterByCep(cepText).collect { response ->
            when(response){
                is Success -> {
                    assertTrue(true)
                }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun filterByCep_shouldReturnSuccessAndEmpty_whenRepositoryPerformsSuccessfully() = runTest {
        coEvery {
            repository.getUnwantedPlacesByCepAndUnwanted(any())
        } answers {
            flow {
                val result = mockUnfavoritePlaceEntries.filter {
                    it.cep == args[0]
                }

                emit(Success(result))
            }
        }

        val cepText = "01234567"
        useCase.filterByCep(cepText).collect { response ->
            when(response){
                is Success -> {
                    assertTrue(true)
                }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun filterByCep_shouldReturnLoading_whenRepositoryIsPerforming() = runTest {
        coEvery {
            repository.getUnwantedPlacesByCepAndUnwanted(any())
        } answers { flow { Loading } }

        val cepText = mockUnfavoritePlaceEntries.first().cep.text
        useCase.filterByCep(cepText).collect { response ->
            when(response){
                is Loading -> { assertTrue(true) }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun filterByCep_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        coEvery {
            repository.getUnwantedPlacesByCepAndUnwanted(any())
        } answers {
            flow { emit(Failure(Exception("Error"))) }
        }

        val cepText = "01234567"
        useCase.filterByCep(cepText).collect { response ->
            when(response){
                is Failure -> {
                    assertTrue(true)
                }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    /** Update Entry **/
    @Test
    fun updateEntry_shouldReturnSuccess_whenRepositoryPerformsSuccessfully() = runTest {
        coEvery {
            repository.updatePlace(any())
        } answers { flow { emit(Success(Unit)) } }

        useCase.updateEntry(mockUnfavoritePlaceEntries.first()).collect { response ->
            when(response){
                is Success -> {
                    assertTrue(true)
                }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun updateEntry_shouldReturnLoading_whenRepositoryIsPerforming() = runTest {
        coEvery {
            repository.updatePlace(any())
        } answers { flow { emit(Loading) } }

        useCase.updateEntry(mockUnfavoritePlaceEntries.first()).collect { response ->
            when(response){
                is Loading -> {
                    assertTrue(true)
                }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }

    @Test
    fun updateEntry_shouldReturnFailure_whenRepositoryPerformsUnsuccessfully() = runTest {
        coEvery {
            repository.updatePlace(any())
        } answers { flow { emit(Failure(Exception("Error"))) } }

        useCase.updateEntry(mockUnfavoritePlaceEntries.first()).collect { response ->
            when(response){
                is Failure -> {
                    assertTrue(true)
                }
                else -> {
                    assertFalse(
                        "It should return a Success response",
                        true
                    )
                }
            }
        }
    }
}