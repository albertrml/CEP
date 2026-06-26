package br.com.arml.cep.model.exception

import android.util.Log
import com.squareup.moshi.JsonDataException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ViaCepException(override val message: String) : Exception(message) {
    class NotFoundException : ViaCepException("Não há endereço para a consulta")
    class InvalidQueryException : ViaCepException("Formato de busca inválido")
    class NetworkOfflineException : ViaCepException("Rede indisponível. Verifique sua conexão.")
    class TooManyRequestsException : ViaCepException("Muitas requisições. Tente novamente em breve.")
    class ServerUnreachableException : ViaCepException("Servidor indisponível ou em manutenção")
    class UnknownException : ViaCepException("Ocorreu um erro inesperado")
}

suspend fun <T> tryConnectionViaCep(f: suspend () -> T): T {
    return try {
        f()
    } catch (e: ViaCepException) {
        throw e
    } catch (e: SocketTimeoutException) {
        Log.e("ViaCep", "Timeout na API", e)
        throw ViaCepException.NetworkOfflineException()
    } catch (e: UnknownHostException) {
        Log.e("ViaCep", "Sem conexão com o host (Offline?)", e)
        throw ViaCepException.NetworkOfflineException()
    } catch (e: IOException) {
        Log.e("ViaCep", "Erro de E/S na rede", e)
        throw ViaCepException.NetworkOfflineException()
    } catch (e: HttpException) {
        val code = e.code()
        Log.e("ViaCep", "Erro HTTP: $code", e)
        when (code) {
            400 -> throw ViaCepException.InvalidQueryException()
            404 -> throw ViaCepException.NotFoundException()
            429 -> throw ViaCepException.TooManyRequestsException()
            in 500..599 -> throw ViaCepException.ServerUnreachableException()
            else -> throw ViaCepException.UnknownException()
        }
    } catch (e: SerializationException) {
        Log.e("ViaCep", "Erro de Parsing (Kotlinx)", e)
        throw ViaCepException.UnknownException()
    } catch (e: JsonDataException) {
        Log.e("ViaCep", "Erro de Parsing (Moshi)", e)
        throw ViaCepException.UnknownException()
    } catch (e: Exception) {
        Log.e("ViaCep", "Erro desconhecido", e)
        throw ViaCepException.UnknownException()
    }
}
